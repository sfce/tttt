package us.cmcc.sms.cleaner;

import android.content.Intent;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import me.sfce.library.activity.BaseTextFragmentTabAdapter;
import me.sfce.library.activity.FragmentTabAdapter;
import me.sfce.library.activity.TabFragmentActivity;
import us.cmcc.sms.cleaner.fragment.WhiteNumberContainer;
import us.cmcc.sms.cleaner.fragment.WhiteWordContainer;

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
            case R.id.add:
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
    protected FragmentTabAdapter getTabAdapter() {
        return new BaseTextFragmentTabAdapter(this, new Class[] { WhiteNumberContainer.class, WhiteWordContainer.class}, new String[] {"手机号", "关键词"});
    }

    @Override
    protected int getContentView() {
        return 0;
    }
}
