package me.sfce.library.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import us.cmcc.sms.cleaner.MenuFragment;
import us.cmcc.sms.cleaner.R;

public abstract class BaseSlidingMenuActivity extends SlidingFragmentActivity {
    public static final String ACTION_HIDE_MENU = "actionHideMenu";
    private int mTitleRes;
    protected ListFragment menuFragment;

    public BaseSlidingMenuActivity(int titleRes) {
        mTitleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(mTitleRes);

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            menuFragment = new MenuFragment();
            t.replace(R.id.menu_frame, menuFragment);
            t.commit();
        } else {
            menuFragment = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        final SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (sm.isMenuShowing()) {
                    sm.toggle();
                }
            }
        }, new IntentFilter(ACTION_HIDE_MENU));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
            default:
                onMenuItemSelected(item.getItemId());
                return true;
        }
    }

    protected abstract void onMenuItemSelected(int id);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(getMenuRes(), menu);
        return true;
    }

    protected abstract int getMenuRes();
}
