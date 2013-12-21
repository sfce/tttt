package me.sfce.library.activity;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockListActivity;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-10-17
 * Time: 下午4:55
 */
public abstract class BaseSherlockListActivity extends SherlockListActivity {
    private int mTitleRes;

    protected BaseSherlockListActivity(int mTitleRes) {
        this.mTitleRes = mTitleRes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(mTitleRes);
        getSherlock().getActionBar().setDisplayShowHomeEnabled(false);
    }
}
