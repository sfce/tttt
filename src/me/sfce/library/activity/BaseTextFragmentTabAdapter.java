package me.sfce.library.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import us.cmcc.sms.cleaner.R;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-14
 * Time: 下午4:06
 */
public class BaseTextFragmentTabAdapter extends BaseFragmentTabAdapter {
    Context context;
    String[] tabNames;

    public BaseTextFragmentTabAdapter(Context context, Class<? extends Fragment>[] classes, String[] tabNames) {
        super(classes);
        this.context = context;
        this.tabNames = tabNames;
    }

    @Override
    public View getIndicator(int index) {
        View view = View.inflate(context, R.layout.main_tab, null);
        TextView indicator = (TextView) view.findViewById(android.R.id.text1);
        indicator.setText(tabNames[index]);
        return view;
    }

    @Override
    public Bundle getData(int index) {
        return null;
    }
}
