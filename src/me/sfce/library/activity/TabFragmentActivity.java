package me.sfce.library.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import me.sfce.library.widget.FixedFragmentTabHost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-10-17
 * Time: 下午4:55
 */
public abstract class TabFragmentActivity extends SherlockFragmentActivity {
    public boolean mStopped;
    protected FixedFragmentTabHost mTabHost;
    protected TabAdapter mAdapter;
    private int mTitleRes;

    protected TabFragmentActivity(int mTitleRes) {
        this.mTitleRes = mTitleRes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(mTitleRes);

        setContentView(getContentView());
        setupTabHost();
    }

    public interface TabAdapter {
        int getCount();

        String getTag(int index);

        View getIndicator(int index);

        Class<?> getClass(int index);

        Bundle getArguments(int index);
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
                    mAdapter.getArguments(i));
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

    class FragmentHolder {
        int containerId;
        BaseFragment fragment;
        String tag;
        boolean add2Backstack;
    }

    protected List<FragmentHolder> fragmentList = new ArrayList<FragmentHolder>();

    @Override
    public void onBackPressed() {
        int index = mTabHost.getCurrentTab();
        BaseContainerFragment container = (BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(mAdapter.getTag(index));
        if (!container.popFragment()) {
            onExit();
        }
    }

    protected abstract void onExit();

    protected abstract TabAdapter getTabAdapter();

    protected abstract int getContentView();

    @Override
    protected void onStart() {
        super.onStart();
        //如果stop状态下存起来的fragment列表不为空，则将他们逐个添加到activity中，注意与onStop的递归
        mStopped = false;
        for (int i = 0; i < fragmentList.size(); i++) {
            FragmentHolder holder = fragmentList.get(i);
            if (replaceTab(holder.containerId, holder.fragment, holder.tag, holder.add2Backstack)) {
                fragmentList.remove(holder);
            } else {
                break;
            }
        }
    }

    @Override
    protected void onStop() {
        /**
         * 当FragmentActivity进入stop状态时，该activity会执行FragmentManagerImpl.dispatchStop();
         * 此方法将mStateSaved置为true，此时如果执行FragmentTransaction.commit()方法，会调用FragmentManagerImpl.checkStateLoss()方法，此方法中有如下代码：
         if (mStateSaved) {
         throw new IllegalStateException(
         "Can not perform this action after onSaveInstanceState");
         }
         *	从而引发"Can not perform this action after onSaveInstanceState" Exception
         */
        mStopped = true;
        super.onStop();
    }

    public boolean replaceTab(int containerId, BaseFragment fragment, String tag, boolean add2Backstack) {
        if (mStopped) {//如何stop，则不能再向activity继续添加fragment，将其存起来，在onStart时恢复。
            FragmentHolder holder = new FragmentHolder();
            holder.containerId = containerId;
            holder.fragment = fragment;
            holder.tag = tag;
            holder.add2Backstack = add2Backstack;
            fragmentList.add(holder);
            return false;
        }
        // TODO hide the IME
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, fragment, tag);
        if (add2Backstack) {
            transaction.addToBackStack(null);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
        manager.executePendingTransactions();
        return true;
    }
}
