package us.cmcc.sms.cleaner;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-10
 * Time: 下午2:53
 */
public class MyApplication extends Application {
    public final static String BAIDU_MAP_KEY = "D3515794D0CAA0CC926F9785C8362C281D5738FC";
    public BMapManager mBMapManager = null;
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initEngineManager(this);
    }

    @Override
    public void onTerminate() {
        if (mBMapManager != null) {
            mBMapManager.destroy();
            mBMapManager = null;
        }
        super.onTerminate();
    }

    public void initEngineManager(Context context) {
        mBMapManager = new BMapManager(context);
        if (!mBMapManager.init(BAIDU_MAP_KEY, new MyGeneralListener())) {
            Toast.makeText(this, "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(MyApplication.getInstance(), "您的网络出错啦！", Toast.LENGTH_LONG).show();
            } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(MyApplication.getInstance(), "输入正确的检索条件！", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                Toast.makeText(MyApplication.getInstance(), "授权Key不正确！", Toast.LENGTH_LONG).show();
            }
        }
    }
}
