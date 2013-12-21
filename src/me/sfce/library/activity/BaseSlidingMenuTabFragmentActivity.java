package me.sfce.library.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TabHost;
import me.sfce.library.widget.FixedFragmentTabHost;
import us.cmcc.sms.cleaner.R;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-14
 * Time: 下午4:31
 */
public abstract class BaseSlidingMenuTabFragmentActivity extends BaseSlidingMenuActivity {
    protected FixedFragmentTabHost mTabHost;
    protected FragmentTabAdapter mAdapter;

    public BaseSlidingMenuTabFragmentActivity(int titleRes) {
        super(titleRes);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                        if (fragment instanceof BaseContainerFragment) {
                            ((BaseContainerFragment) fragment).clearBackStack();

                        }
                    } else {
                        mTabHost.setCurrentTab(index);
                    }
                }
            });
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                BaseSlidingMenuTabFragmentActivity.this.onTabChanged(mTabHost.getCurrentTab());
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tabId);
                if (fragment instanceof BaseContainerFragment && null != fragment) {
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
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(mAdapter.getTag(index));
        if (fragment instanceof BaseContainerFragment) {
            BaseContainerFragment container = (BaseContainerFragment) fragment;
            if (!container.popFragment()) {
                onExit();
            }
        } else {
            onExit();
        }
    }

    protected abstract void onExit();

    protected abstract FragmentTabAdapter getTabAdapter();

    protected int getContentView() {
        return R.layout.activity_tab;
    }
}
