package us.cmcc.sms.cleaner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午2:00
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SmsReceiver receiver = new SmsReceiver();
            context.registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
    }
}
