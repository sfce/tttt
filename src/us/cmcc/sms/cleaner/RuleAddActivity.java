package us.cmcc.sms.cleaner;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockActivity;
import us.cmcc.sms.cleaner.dao.BlackNumber;
import us.cmcc.sms.cleaner.dao.BlackNumberDao;
import us.cmcc.sms.cleaner.dao.DaoMaster;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-11
 * Time: 下午9:34
 */
public class RuleAddActivity extends SherlockActivity {
    public final static int TYPE_BLACK_NUMBER = 0;
    public final static int TYPE_BLACK_WORD = 0;
    public final static int TYPE_WHITE_NUMBER = 0;
    public final static int TYPE_WHITE_WORD = 0;

    private boolean type;
    private EditText text1;
    private EditText text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rule_add);
        text1 = (EditText) findViewById(android.R.id.text1);
        text2 = (EditText) findViewById(android.R.id.text2);
        String hint = getIntent().getStringExtra("hint");
        text1.setHint(hint);
        if (getIntent().getIntExtra("inputType", 0) == 1) {
            text1.setKeyListener(DigitsKeyListener.getInstance("0123456789.*#"));
        }
        type = "number".equals(getIntent().getStringExtra("type"));
    }

    public void save(View v) {
        // TODO校验
        DaoMaster.DevOpenHelper dbOpenHelper = new DaoMaster.DevOpenHelper(this, MyApplication.DB_NAME, null);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        BlackNumberDao dao = daoMaster.newSession().getBlackNumberDao();
        dao.insert(new BlackNumber(text1.getText().toString(), text2.getText().toString()));
        finish();
    }

    public void cancel(View v) {
        finish();
    }
}
