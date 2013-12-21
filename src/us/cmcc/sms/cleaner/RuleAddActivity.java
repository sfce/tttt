package us.cmcc.sms.cleaner;

import android.content.Intent;
import us.cmcc.sms.cleaner.dao.BlackNumber;
import us.cmcc.sms.cleaner.dao.BlackNumberDao;
import us.cmcc.sms.cleaner.dao.BlackWord;
import us.cmcc.sms.cleaner.dao.BlackWordDao;
import us.cmcc.sms.cleaner.dao.DaoMaster;
import us.cmcc.sms.cleaner.dao.WhiteNumber;
import us.cmcc.sms.cleaner.dao.WhiteNumberDao;
import us.cmcc.sms.cleaner.dao.WhiteWord;
import us.cmcc.sms.cleaner.dao.WhiteWordDao;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * Created with IntelliJ IDEA. User: sfce Date: 13-12-11 Time: 下午9:34
 */
public class RuleAddActivity extends SherlockActivity {
	public final static int TYPE_BLACK_NUMBER = 0;
	public final static int TYPE_BLACK_WORD = 1;
	public final static int TYPE_WHITE_NUMBER = 2;
	public final static int TYPE_WHITE_WORD = 3;

    public final static String ACTION_RULE_ADD = "action_rule_add";

	private EditText text1;
	private EditText text2;
	private int business;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getSherlock().getActionBar().setDisplayShowHomeEnabled(false);
		setContentView(R.layout.rule_add);
		text1 = (EditText) findViewById(android.R.id.text1);
		text2 = (EditText) findViewById(android.R.id.text2);
		String hint = getIntent().getStringExtra("hint");
		text1.setHint(hint);
		if (getIntent().getIntExtra("inputType", 0) == 1) {
			text1.setKeyListener(DigitsKeyListener.getInstance("0123456789.*#"));
		}
		business = getIntent().getIntExtra("business", 0);
        switch (business) {
            case TYPE_WHITE_NUMBER:
                setTitle("添加白名单");
                break;
            case TYPE_BLACK_NUMBER:
                setTitle("添加黑名单");
                break;
            case TYPE_BLACK_WORD:
                setTitle("添加关键词");
                break;
        }
    }

	public void save(View v) {
		// TODO校验
		DaoMaster.DevOpenHelper dbOpenHelper = new DaoMaster.DevOpenHelper(this, MyApplication.DB_NAME, null);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);

		switch (business) {
		case TYPE_BLACK_NUMBER:
			BlackNumberDao blackNumberDao = daoMaster.newSession().getBlackNumberDao();
			blackNumberDao.insert(new BlackNumber(text1.getText().toString(), text2.getText().toString()));
			break;
		case TYPE_BLACK_WORD:
			BlackWordDao blackWordDao = daoMaster.newSession().getBlackWordDao();
			blackWordDao.insert(new BlackWord(text1.getText().toString(), text2.getText().toString()));
			break;
		case TYPE_WHITE_NUMBER:
			WhiteNumberDao whiteNumberDao = daoMaster.newSession().getWhiteNumberDao();
			whiteNumberDao.insert(new WhiteNumber(text1.getText().toString(), text2.getText().toString()));
			break;
		case TYPE_WHITE_WORD:
			WhiteWordDao whiteWordDao = daoMaster.newSession().getWhiteWordDao();
			whiteWordDao.insert(new WhiteWord(text1.getText().toString(), text2.getText().toString()));
			break;
		}
        sendBroadcast(new Intent(ACTION_RULE_ADD));
		finish();
	}
	
	public void cancel(View v) {
		finish();
	}
}
