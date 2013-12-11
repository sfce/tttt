package us.cmcc.sms.cleaner.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MapViewEx extends MapView {
	public interface TapListener {
		void onTap(GeoPoint point);
	}

	TapListener tapListener;

	public void registerTapListener(TapListener tapListener) {
		this.tapListener = tapListener;
	}

	public MapViewEx(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	public MapViewEx(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
	}

	public MapViewEx(Context arg0) {
		super(arg0);
	}

	private int x;
	private int y;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			this.x = x;
			this.y = y;
		} else if (action == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			if (Math.abs(this.x - x) < 20 && Math.abs(this.y - y) < 20) {
				GeoPoint pt = super.getProjection().fromPixels(x, y);
				if (null != tapListener) {
					tapListener.onTap(pt);
				}
			}
		}
		return super.onTouchEvent(event);
	}

}
