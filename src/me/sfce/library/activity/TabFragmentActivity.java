package me.sfce.library.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import me.sfce.library.widget.FixedFragmentTabHost;
import us.cmcc.sms.cleaner.R;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-10-17
 * Time: 下午4:55
 */
public abstract class TabFragmentActivity extends SherlockFragmentActivity {
    protected FixedFragmentTabHost mTabHost;
    protected FragmentTabAdapter mAdapter;
    private int mTitleRes;

    protected TabFragmentActivity(int mTitleRes) {
        this.mTitleRes = mTitleRes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(mTitleRes);
        getSherlock().getActionBar().setDisplayShowHomeEnabled(false);

        setContentView(getContentView());
        setupTabHost();
    }

    private void setupTabHost() {
        mTabHost = (FixedFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mAdapter = getTabAdapter();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View indicator = mAdapter.getIndicator(i);
            final String tag = mAdapter.getTag(i);
            mTabHost.addTab(
                    mTabHost.newTabSpec(tag).setIndicator(indicator),
                    mAdapter.getClass(i),
                    mAdapter.getData(i));
            final int index = i;
            indicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index == mTabHost.getCurrentTab()) {
                        BaseContainerFragment containerFragment = (BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(tag);
                        containerFragment.clearBackStack();
                    } else {
                        mTabHost.setCurrentTab(index);
                    }
                }
            });
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                TabFragmentActivity.this.onTabChanged(mTabHost.getCurrentTab());
                BaseContainerFragment containerFragment = (BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(tabId);
                if (null != containerFragment) {
                    // TODO
                }
            }
        });
    }

    protected void onTabChanged(int index) {

    }

    public BaseContainerFragment getChildContainer(int index) {
        BaseContainerFragment containerFragment = (BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(mAdapter.getTag(index));
        return containerFragment;
    }

    public void changeToTab(int index) {
        mTabHost.setCurrentTab(index);
    }

    protected int getCurrentTab() {
        return mTabHost.getCurrentTab();
    }

    @Override
    public void onBackPressed() {
        int index = mTabHost.getCurrentTab();
        BaseContainerFragment container = (BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(mAdapter.getTag(index));
        if (!container.popFragment()) {
            onExit();
        }
    }

    protected abstract void onExit();

    protected abstract FragmentTabAdapter getTabAdapter();

    protected int getContentView() {
        return R.layout.activity_tab;
    }
}
