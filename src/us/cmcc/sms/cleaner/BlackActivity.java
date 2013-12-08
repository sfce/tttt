package us.cmcc.sms.cleaner;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import me.sfce.library.activity.TabFragmentActivity;
import us.cmcc.sms.cleaner.fragment.BlackNumberContainer;
import us.cmcc.sms.cleaner.fragment.BlackWordContainer;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午7:05
 */
public class BlackActivity extends TabFragmentActivity {
    public BlackActivity() {
        super(R.string.black_list);
    }

    @Override
    protected void onExit() {
    }

    private final class TabInfo {
        String tag;
        int icon;
        Class<?> clazz;

        public TabInfo(String tag, int icon, Class<?> clazz) {
            this.tag = tag;
            this.icon = icon;
            this.clazz = clazz;
        }
    }

    @Override
    protected TabAdapter getTabAdapter() {
        final TabInfo[] tabs = new TabInfo[]{
                new TabInfo("手机号", R.drawable.abs__ic_go, BlackNumberContainer.class),
                new TabInfo("关键词", R.drawable.abs__ic_go, BlackWordContainer.class)
        };
        return new TabAdapter() {
            @Override
            public int getCount() {
                return tabs.length;
            }

            @Override
            public String getTag(int index) {
                return tabs[index].tag;
            }

            @Override
            public View getIndicator(int index) {
                View view = View.inflate(BlackActivity.this, R.layout.main_tab, null);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(getTag(index));
                text.setCompoundDrawablesWithIntrinsicBounds(0, tabs[index].icon, 0, 0);
                return view;
            }

            @Override
            public Class<?> getClass(int index) {
                return tabs[index].clazz;
            }

            @Override
            public Bundle getArguments(int index) {
                return null;
            }
        };
    }

    @Override
    protected int getContentView() {
        return R.layout.black;
    }
}
