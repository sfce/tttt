package me.sfce.library.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-10-17
 * Time: 下午5:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseFragment extends Fragment {
    protected View contentView;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == contentView) {
            contentView = onPrepareView(inflater, savedInstanceState);
        } else {
            ((ViewGroup) contentView.getParent()).removeView(contentView);
        }
        return contentView;
    }

    protected abstract View onPrepareView(LayoutInflater inflater, Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String uniqueTag(Class<? extends BaseFragment> clazz) {
        return clazz.getName();
    }

    public String uniqueTag() {
        return getClass().getName();
    }

    public TabFragmentActivity getRootParent() {
        return (TabFragmentActivity) getActivity();
    }

}
