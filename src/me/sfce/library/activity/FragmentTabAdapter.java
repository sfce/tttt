package me.sfce.library.activity;

import android.os.Bundle;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-14
 * Time: 下午3:55
 */
public interface FragmentTabAdapter {
    int getCount();

    String getTag(int index);

    View getIndicator(int index);

    Class<?> getClass(int index);

    Bundle getData(int index);
}
