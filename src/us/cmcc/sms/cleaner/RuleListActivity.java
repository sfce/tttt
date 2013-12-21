package us.cmcc.sms.cleaner;

import android.app.AlertDialog;
import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import de.greenrobot.dao.AbstractDao;
import us.cmcc.sms.cleaner.dao.DaoMaster;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午7:05
 */
public class RuleListActivity extends SherlockListActivity {
    private int type;
    private AbstractDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);
        getSherlock().getActionBar().setDisplayShowHomeEnabled(false);
        type = getIntent().getIntExtra("type", 0);
        TextView empty = (TextView) findViewById(android.R.id.empty);
        DaoMaster.DevOpenHelper dbOpenHelper = new DaoMaster.DevOpenHelper(this, MyApplication.DB_NAME, null);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        switch (type) {
            case RuleAddActivity.TYPE_BLACK_NUMBER:
                setTitle(R.string.black_list);
                dao = daoMaster.newSession().getBlackNumberDao();
                empty.setText("展示在这里的手机号的短信将被拦截");
                break;
            case RuleAddActivity.TYPE_BLACK_WORD:
                setTitle(R.string.key_word);
                dao = daoMaster.newSession().getBlackWordDao();
                empty.setText("展示在这里的手机号的短信将被放行");
                break;
            case RuleAddActivity.TYPE_WHITE_NUMBER:
                setTitle(R.string.white_list);
                dao = daoMaster.newSession().getWhiteNumberDao();
                empty.setText("包含展示在这里的关键词的短信将被拦截");
                break;
        }
        setListAdapter(new ArrayAdapter(this, R.layout.text, dao.loadAll()));
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setListAdapter(new ArrayAdapter(RuleListActivity.this, R.layout.text, dao.loadAll()));
            }
        }, new IntentFilter(RuleAddActivity.ACTION_RULE_ADD));
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        new AlertDialog.Builder(this).setItems(new String[]{"修改", "删除", "短信", "呼叫"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        break;
                    case 1:
                        dao.delete(dao.loadAll().get(position));
                        setListAdapter(new ArrayAdapter(RuleListActivity.this, R.layout.text, dao.loadAll()));
                        break;

                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        }).create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this, RuleAddActivity.class);
                switch (type) {
                    case RuleAddActivity.TYPE_WHITE_NUMBER:
                        intent.putExtra("hint", "电话号码(必填)");
                        intent.putExtra("inputType", 1);
                        break;
                    case RuleAddActivity.TYPE_BLACK_NUMBER:
                        intent.putExtra("hint", "电话号码(必填)");
                        intent.putExtra("inputType", 1);
                        break;
                    case RuleAddActivity.TYPE_BLACK_WORD:
                        intent.putExtra("hint", "关键字(必填)");
                        break;
                }
                intent.putExtra("business", type);
                startActivity(intent);
                    /*intent.putExtra("hint", "关键字(必填)");
                    intent.putExtra("inputType", 2);
                    intent.putExtra("type", "word");
                    intent.putExtra("business", RuleAddActivity.TYPE_BLACK_WORD);
                    startActivity(intent);*/
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
