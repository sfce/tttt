package me.sfce.library.activity;

import android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-10-18
 * Time: 下午3:56
 */
public abstract class BaseContainerFragment extends BaseFragment {
    public <T> T findFragment(Class<? extends BaseFragment> clazz) {
        return (T) findFragment(uniqueTag(clazz));
    }

    public BaseFragment findFragment(String tag) {
        return (BaseFragment) getChildFragmentManager().findFragmentByTag(tag);
    }

    public void replaceFragment(Fragment fragment, String tag, boolean add2BackStack) {
        TabFragmentActivity host = (TabFragmentActivity) getActivity();
        if (host.mStopped) {
            return;
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment, tag);
        if (add2BackStack) {
            transaction.addToBackStack(null);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public boolean popFragment() {
        Log.i("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
            getChildFragmentManager().executePendingTransactions();
        }
        return isPop;
    }

    public void clearBackStack() {
        while (popFragment()) ;
    }

    // To customize your ui, extends this method in the children
    @Override
    protected final View onPrepareView(LayoutInflater inflater, Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(inflater.getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frameLayout.setId(R.id.content);
        return frameLayout;
    }

    private boolean inited;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!inited) {
            init();
            inited = true;
        }
    }

    protected abstract void init();
}

