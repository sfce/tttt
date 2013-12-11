package us.cmcc.sms.cleaner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.github:
                int currentTab = getCurrentTab();
                if (currentTab == 0) {
                    Intent intent = new Intent(this, RuleAddActivity.class);
                    intent.putExtra("hint", "电话号码(必填)");
                    intent.putExtra("inputType", 1);
                    intent.putExtra("type", "number");
                    intent.putExtra("business", RuleAddActivity.TYPE_BLACK_NUMBER);
                    startActivity(intent);
                } else {

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onExit() {
        finish();
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
