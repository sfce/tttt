package us.cmcc.sms.cleaner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import us.cmcc.sms.cleaner.bean.Sms;
import us.cmcc.sms.cleaner.filter.*;
import us.cmcc.sms.cleaner.filter.dao.ISmsDao;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午2:00
 */
public class SmsReceiver extends BroadcastReceiver {
    PowerManager.WakeLock wakeLock = null;
    Object lock = new Object();

    private void newWakeLock(Context context) {
        synchronized (lock) {
            if (wakeLock == null) {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "prepareNotify");
                wakeLock.setReferenceCounted(false);
            }
            wakeLock.acquire();
        }
    }

    private void releaseWakeLock() {
        synchronized (lock) {
            if (wakeLock != null) {
                wakeLock.release();
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO 总开关
        StringBuffer body = new StringBuffer();
        String number = null;
        long time = 0;
        newWakeLock(context);
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : pdus) {
                byte[] pdu = (byte[]) obj;
                SmsMessage sms = SmsMessage.createFromPdu(pdu);
                number = sms.getOriginatingAddress();
                body.append(sms.getMessageBody());
                time = sms.getTimestampMillis();
            }
        }
        if (null == number) return;

        //去除country code
        String MCC = getMCC(context);
        if (number.startsWith(MCC)) {
            number = number.substring(MCC.length());
        }

        Sms sms = new Sms(number, body.toString().replaceAll("\\s", "").toLowerCase(), time);
        //时间段-->联系人-->白号码-->白关键词-->黑号码-->黑关键词-->联系人-->云过滤
        ISmsFilter smartFilter = new SmartFilter(null);
        ISmsFilter contactFilter = new ContactFilter(smartFilter);
        ISmsFilter blackWordFilter = new BlackWordFilter(contactFilter);
        ISmsFilter blackNumberFilter = new BlackNumberFilter(blackWordFilter);
        ISmsFilter whiteWordFilter = new WhiteWordFilter(blackNumberFilter);
        ISmsFilter whiteNumberFilter = new WhiteNumberFilter(whiteWordFilter);
        ISmsFilter settingContactFilter = new ContactFilter(whiteNumberFilter);
        ISmsFilter periodFilter = new PeriodFilter(settingContactFilter);
        boolean result = periodFilter.dispatch(context, sms);
        if (result) {
            //TODO
            ISmsDao dao = null;
            dao.add(sms);
            abortBroadcast();
        }

        releaseWakeLock();
    }

    public static String getMCC(Context context) {
        String MCC_MNC = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
        if (MCC_MNC != null) {
            if (MCC_MNC.startsWith("460")) {//大陆
                return "+86";
            } else if (MCC_MNC.startsWith("454")) {//香港
                return "+852";
            } else if (MCC_MNC.startsWith("455")) {//澳门
                return "+853";
            } else if (MCC_MNC.startsWith("466")) {//台湾
                return "+886";
            }
        }
        return "+86";
    }

}
