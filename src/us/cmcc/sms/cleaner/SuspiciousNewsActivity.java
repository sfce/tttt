package us.cmcc.sms.cleaner;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-9
 * Time: 下午11:48
 */
public class SuspiciousNewsActivity extends SherlockFragmentActivity {
    private MapView mMapView = null;
    MyLocationOverlay myLocationOverlay = null;
    LocationData locData = null;
    LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;

            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            myLocationOverlay.setData(locData);
            if (null != mMapView) {
                GeoPoint locationPoint = new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6));
                mMapView.getController().setCenter(locationPoint);
                mMapView.refresh();
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle("可疑动态");
        getSherlock().getActionBar().setDisplayShowHomeEnabled(false);
        MyApplication app = MyApplication.getInstance();
        if (app.mBMapManager == null) {
            app.initEngineManager(this);
        }
        setContentView(R.layout.mapview);
        // 初始化地图View
        mMapView = (MapView) findViewById(R.id.mapview);

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setPriority(LocationClientOption.NetWorkFirst);
        option.setCoorType("bd09ll");
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocClient.requestLocation();

        mMapView.setBuiltInZoomControls(true);
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        myLocationOverlay = new MyLocationOverlay(mMapView);
        locData = new LocationData();
        myLocationOverlay.setData(locData);
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        mMapView.refresh();

        refreshMap();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mLocClient != null)
            mLocClient.stop();
        mMapView.destroy();
        MyApplication app = MyApplication.getInstance();
        if (app.mBMapManager != null) {
            app.mBMapManager.destroy();
            app.mBMapManager = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }

    private class Item {
        int lac;
        long time;
        double latitude;
        double longitude;

        private Item(int lac, long time, double latitude, double longitude) {
            this.lac = lac;
            this.time = time;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private double mathScale(double lat) {
        return ((int) (lat * 1000000)) / 1000000.0d;
    }

    private void refreshMap() {
        final ArrayList<Item> items = new ArrayList<Item>();
        for (int i = 0; i < 1000; i++) {
            int lac = (int) (Math.random() * 10000 + 10000);
            int count = ((int) (Math.random() * 10000)) % 3 + 1;
            ArrayList<Integer> cids = new ArrayList<Integer>(count);
            for (int j = 0; j < count; j++) {
                cids.add((int) (Math.random() * 20000 + 10000));
            }
            double lat = Math.random() * (34.857971 - 34.668612) + 34.668612;
            lat = mathScale(lat);
            double lon = Math.random() * (113.812738 - 113.466424) + 113.466424;
            lon = mathScale(lon);

            long time = System.currentTimeMillis() - (long) (1000 * 3600 * Math.random());
            items.add(new Item(lac, time, lat, lon));
        }
        if (null != mMapView) {
            List<Overlay> overlays = mMapView.getOverlays();
            for (Overlay overlay : overlays) {
                if (overlay instanceof LacOverlay) {
                    overlays.remove(overlay);
                }
            }
            overlays.add(new LacOverlay(mMapView, items));
        }
        mMapView.refresh();
        mMapView.onResume();
    }

    public class LacOverlay extends ItemizedOverlay<OverlayItem> {
        private final int RADIUS = 48;
        List<Item> items;

        public LacOverlay(MapView arg1, List<Item> items) {
            super(null, arg1);
            this.items = items;
            long now = System.currentTimeMillis();
            for (Item data : items) {
                if (null != data) {
                    GeoPoint point = new GeoPoint((int) (data.latitude * 1E6), (int) (data.longitude * 1E6));
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(data.time));
                    OverlayItem item = new OverlayItem(point, data.lac + "", time);
                    if (now - data.time > 1000 * 60 * 30) {
                        item.setMarker(arg1.getContext().getResources().getDrawable(R.drawable.map_item_1));
                    } else {
                        item.setMarker(arg1.getContext().getResources().getDrawable(R.drawable.map_item_2));
                    }
                    addItem(item);
                }
            }
        }

        private View popView;

        @Override
        public boolean onTap(GeoPoint p, MapView mapView) {
            if (null == popView) {
                LayoutInflater inflater = LayoutInflater.from(SuspiciousNewsActivity.this);
                popView = inflater.inflate(R.layout.overlay_pop, null);
                popView.setBackgroundResource(R.drawable.bubble_background);
                mMapView.addView(popView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                        MapView.LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.BOTTOM_CENTER));
            }
            Projection projection = mapView.getProjection();
            Point point1 = new Point();
            projection.toPixels(p, point1);
            Point point2 = new Point();
            for (final Item data : items) {
                GeoPoint point = new GeoPoint((int) (data.latitude * 1E6), (int) (data.longitude * 1E6));
                projection.toPixels(point, point2);
                if (Math.abs(point1.x - point2.x) < RADIUS && Math.abs(point1.y - point2.y) < RADIUS) {
                    mMapView.updateViewLayout(popView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                            MapView.LayoutParams.WRAP_CONTENT, point, MapView.LayoutParams.BOTTOM_CENTER));
                    popView.setVisibility(View.VISIBLE);
                    Button ok = (Button) popView.findViewById(R.id.ok);
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(data.time));
                    ok.setText(data.lac + "|" + time);
                    return true;
                }
            }
            popView.setVisibility(View.GONE);
            return false;
        }
    }

}

