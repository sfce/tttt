package us.cmcc.sms.cleaner.util;

import android.app.ProgressDialog;
import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-10
 * Time: 下午9:48
 */
public class LocationUtil implements BDLocationListener {
    ProgressDialog loading;
    Context context;
    private LocationClient mLocClient;
    LocationListener listener;

    public LocationUtil setListener(LocationListener listener) {
        this.listener = listener;
        mLocClient.start();
        return this;
    }

    public void start() {
        mLocClient.requestLocation();
    }

    public LocationUtil(Context context, String loadingMsg) {
        this.context = context;
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setPriority(LocationClientOption.NetWorkFirst);
        option.setCoorType("bd09ll"); // 设置坐标类型
        mLocClient.setLocOption(option);
        if (null != loadingMsg)
            loading = ProgressDialog.show(context, "", loadingMsg);
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (null != loading)
            loading.dismiss();
        mLocClient.stop();
        if (location == null) {
            listener.onLocationResult(false, null);
            return;
        }
        listener.onLocationResult(true, String.format("%f,%f", location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onReceivePoi(BDLocation bdLocation) {
    }
}
