package us.cmcc.sms.cleaner;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import us.cmcc.sms.cleaner.widget.MapViewEx;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-10
 * Time: 下午2:59
 */
public class MapActivity extends Activity {
    private MapViewEx mMapView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        // 初始化MapManager
        final MyApplication app = MyApplication.getInstance();
        if (app.mBMapManager == null) {
            app.initEngineManager(this);
        }
        app.mBMapManager.start();
        setContentView(R.layout.mapview);
        mMapView = (MapViewEx) findViewById(R.id.mapview);
        mMapView.setBuiltInZoomControls(true);
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.refresh();

        String lStr = getIntent().getStringExtra("location");
        String[] ll = lStr.split(",");
        GeoPoint locationPoint = new GeoPoint((int) (Double.valueOf(ll[0]) * 1e6), (int) (Double.valueOf(ll[1]) * 1e6));
        mMapView.getController().setCenter(locationPoint);
        addSelectLocationOverlay(locationPoint);
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
        mMapView.getOverlays().clear();
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


    private void addSelectLocationOverlay(GeoPoint locationPoint) {
        mMapView.getOverlays().add(new MySelectLocationOverlay(locationPoint, mMapView));
        mMapView.refresh();
    }

    public class MySelectLocationOverlay extends ItemizedOverlay<OverlayItem> {
        private GeoPoint geoPoint;
        private View popView;

        public MySelectLocationOverlay(GeoPoint curGeoPoint, MapView mapView) {
            super(null, mapView);
            LayoutInflater inflater = LayoutInflater.from(MapActivity.this);
            popView = inflater.inflate(R.layout.overlay_pop, null);
            mapView.addView(popView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                    MapView.LayoutParams.WRAP_CONTENT, curGeoPoint, MapView.LayoutParams.BOTTOM_CENTER));
            ((Button) popView.findViewById(R.id.ok)).setText(getIntent().getStringExtra("data"));
            /*popView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final StringBuffer result = new StringBuffer();
                    final GeoPoint gp = geoPoint;
                    result.append(gp.getLatitudeE6() / 1E6);
                    result.append(",");
                    result.append(gp.getLongitudeE6() / 1E6);
                    new AlertDialog.Builder(MapActivity.this)//
                            .setTitle("提示")//
                            .setMessage("已选中坐标：" + result)//
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent data = new Intent();
                                    data.putExtra("lan", gp.getLatitudeE6() / 1E6);
                                    data.putExtra("lon", gp.getLongitudeE6() / 1E6);
                                    setResult(RESULT_OK, data);
                                    dialog.dismiss();
                                    finish();
                                }
                            }).setNegativeButton("重选", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            });*/

            this.geoPoint = curGeoPoint;
        }

        @Override
        public boolean onTap(GeoPoint p, MapView mapView) {
            this.geoPoint = p;
//            mMapView.updateViewLayout(popView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
//                    MapView.LayoutParams.WRAP_CONTENT, p, MapView.LayoutParams.BOTTOM_CENTER));
            return true;
        }
    }

}
