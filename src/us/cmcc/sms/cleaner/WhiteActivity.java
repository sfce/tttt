package us.cmcc.sms.cleaner;

import me.sfce.library.activity.TabFragmentActivity;
import us.cmcc.sms.cleaner.fragment.WhiteNumberContainer;
import us.cmcc.sms.cleaner.fragment.WhiteWordContainer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * 白名单处理
 */
public class WhiteActivity extends TabFragmentActivity {
    public WhiteActivity() {
        super(R.string.white_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.github:
                int currentTab = getCurrentTab();
                Intent intent = new Intent(this, RuleAddActivity.class);
                if (currentTab == 0) {
                    intent.putExtra("hint", "电话号码(必填)");
                    intent.putExtra("inputType", 1);
                    intent.putExtra("type", "number");
                    intent.putExtra("business", RuleAddActivity.TYPE_WHITE_NUMBER);
                    startActivity(intent);
                } else {
                	intent.putExtra("hint", "关键字(必填)");
                    intent.putExtra("inputType", 2);
                    intent.putExtra("type", "word");
                    intent.putExtra("business", RuleAddActivity.TYPE_WHITE_WORD);
                    startActivity(intent);
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
                new TabInfo("手机号", R.drawable.abs__ic_go, WhiteNumberContainer.class),
                new TabInfo("关键词", R.drawable.abs__ic_go, WhiteWordContainer.class)
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
                View view = View.inflate(WhiteActivity.this, R.layout.main_tab, null);
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
