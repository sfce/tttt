package us.cmcc.sms.cleaner.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import me.sfce.library.activity.BaseFragment;
import us.cmcc.sms.cleaner.R;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-14
 * Time: 下午4:41
 */
public class InterceptListFragment extends BaseFragment {
    @Override
    protected View onPrepareView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_list, null);
    }
}
