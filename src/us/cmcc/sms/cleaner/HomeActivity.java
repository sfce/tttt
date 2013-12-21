package us.cmcc.sms.cleaner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import me.sfce.library.Global;
import me.sfce.library.activity.BaseSlidingMenuTabFragmentActivity;
import me.sfce.library.activity.BaseTextFragmentTabAdapter;
import me.sfce.library.activity.FragmentTabAdapter;
import us.cmcc.sms.cleaner.fragment.IncomeListFragment;
import us.cmcc.sms.cleaner.fragment.InterceptListFragment;

public class HomeActivity extends BaseSlidingMenuTabFragmentActivity {
    private boolean exiting;
    private HandlerEx handler;

    private final static class HandlerEx extends Handler {
        private static final int EXIT = 0;
        private static final int EXIT_CANCELED = 1;
        HomeActivity context;

        HandlerEx(HomeActivity context) {
            super();
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXIT_CANCELED:
                    context.exiting = false;
                    break;
                case EXIT:
                    context.finish();
                    break;
            }
        }
    }

    public HomeActivity() {
        super(R.string.sms_list);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSlidingActionBarEnabled(true);
        handler = new HandlerEx(this);
        Global.Update.checkNewVersion(this, true);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!pref.contains("first")) {
            startService(new Intent(this, ReportService.class));
            pref.edit().putBoolean("first", true).commit();
        }
        getSherlock().getActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    protected void onMenuItemSelected(int id) {
    }

    @Override
    protected int getMenuRes() {
        return R.menu.empty;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            getSlidingMenu().toggle();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!getSlidingMenu().isMenuShowing()) {
                getSlidingMenu().toggle();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onExit() {
        /*if (exiting) {
            finish();
            handler.sendEmptyMessageDelayed(HandlerEx.EXIT, 100);
        } else {
            IcsToast.makeText(this, "再按一次返回键退出", IcsToast.LENGTH_LONG).show();
            exiting = true;
            handler.sendEmptyMessageDelayed(HandlerEx.EXIT_CANCELED, 2000);
        }*/
    }

    @Override
    protected FragmentTabAdapter getTabAdapter() {
        Class<? extends Fragment> fragments[] = new Class[2];
        fragments[0] = InterceptListFragment.class;
        fragments[1] = IncomeListFragment.class;
        return new BaseTextFragmentTabAdapter(this, fragments, new String[]{"已拦截", "收件箱"});
    }
}
