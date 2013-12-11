package us.cmcc.sms.cleaner.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import us.cmcc.sms.cleaner.MapActivity;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-10
 * Time: 下午9:48
 */
public class LocationListenner implements BDLocationListener {
    ProgressDialog loading;
    Context context;
    private LocationClient mLocClient;

    public LocationListenner(Context context) {
        this.context = context;
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setPriority(LocationClientOption.NetWorkFirst);
        option.setCoorType("bd09ll"); // 设置坐标类型
        mLocClient.setLocOption(option);
        mLocClient.start();
        loading = ProgressDialog.show(context, "", "正在获取位置信息...");
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        loading.dismiss();
        if (location == null) {
            new AlertDialog.Builder(context)
                    .setTitle("提示").setMessage("定位失败")
                    .setPositiveButton(android.R.string.ok, null).create().show();
            return;
        }
        mLocClient.stop();
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("location", String.format("%f,%f", location.getLatitude(), location.getLongitude()));
        context.startActivity(intent);
    }

    @Override
    public void onReceivePoi(BDLocation bdLocation) {
    }
}
