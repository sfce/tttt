package us.cmcc.sms.cleaner.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import me.sfce.library.activity.BaseFragment;
import us.cmcc.sms.cleaner.R;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午8:03
 */
public class BlackNumberFragment extends BaseFragment {
    @Override
    protected View onPrepareView(LayoutInflater inflater, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_tab, null);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText("黑名单号码");
        return view;
    }
}
