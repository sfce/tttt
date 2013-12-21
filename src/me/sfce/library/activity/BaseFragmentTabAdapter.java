package me.sfce.library.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-14
 * Time: 下午4:03
 */
public abstract class BaseFragmentTabAdapter implements FragmentTabAdapter {
    private Class<? extends Fragment> classes[];

    public BaseFragmentTabAdapter(Class<? extends Fragment>[] classes) {
        this.classes = classes;
    }

    @Override
    public int getCount() {
        return classes.length;
    }

    @Override
    public String getTag(int index) {
        return classes[index].getName();
    }

    @Override
    public abstract View getIndicator(int index);

    @Override
    public Class<?> getClass(int index) {
        return classes[index];
    }

    @Override
    public abstract Bundle getData(int index);
}
