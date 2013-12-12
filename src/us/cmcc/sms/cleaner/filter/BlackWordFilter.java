package us.cmcc.sms.cleaner.filter;

import java.util.List;

import us.cmcc.sms.cleaner.MyApplication;
import us.cmcc.sms.cleaner.bean.Sms;
import us.cmcc.sms.cleaner.dao.BlackWord;
import us.cmcc.sms.cleaner.dao.BlackWordDao;
import us.cmcc.sms.cleaner.dao.DaoMaster;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午6:11
 */
public class BlackWordFilter extends SmsFilter {
    public BlackWordFilter(ISmsFilter next) {
        super(next);
    }

    @Override
    public FilterResult doFilter(Context context, Sms sms) {
    	DaoMaster.DevOpenHelper dbOpenHelper = new DaoMaster.DevOpenHelper(context, MyApplication.DB_NAME, null);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        BlackWordDao dao = daoMaster.newSession().getBlackWordDao();
        List<BlackWord> blackWords = dao.loadAll();
        
        String content = sms.getBody();
        for (int i = 0; i < blackWords.size(); i++) {
        	String blackWord = blackWords.get(i).getWord();
        	//规则1：包含字符的
        	if(content.contains(blackWord))return FilterResult.RED;
        	
        	//其它规则 TODO
			
		}
        
        return FilterResult.YELLOW;
    }

    @Override
    public boolean sizeUp(Context context) {
        return false;
    }
}
