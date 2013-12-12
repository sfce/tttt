package us.cmcc.sms.cleaner.fragment;

import me.sfce.library.activity.BaseContainerFragment;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午7:26
 */
public class WhiteNumberContainer extends BaseContainerFragment {
    @Override
    protected void init() {
        replaceFragment(new WhiteNumberFragment(), uniqueTag(WhiteNumberFragment.class), false);
    }
}
