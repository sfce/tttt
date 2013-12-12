package us.cmcc.sms.cleaner.filter;

import java.util.List;

import us.cmcc.sms.cleaner.MyApplication;
import us.cmcc.sms.cleaner.bean.Sms;
import us.cmcc.sms.cleaner.dao.DaoMaster;
import us.cmcc.sms.cleaner.dao.WhiteWord;
import us.cmcc.sms.cleaner.dao.WhiteWordDao;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午6:11
 */
public class WhiteWordFilter extends SmsFilter {
    public WhiteWordFilter(ISmsFilter next) {
        super(next);
    }

    @Override
    public FilterResult doFilter(Context context, Sms sms) {
    	DaoMaster.DevOpenHelper dbOpenHelper = new DaoMaster.DevOpenHelper(context, MyApplication.DB_NAME, null);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        WhiteWordDao dao = daoMaster.newSession().getWhiteWordDao();
        List<WhiteWord> whiteWords = dao.loadAll();
        
        String content = sms.getBody();
        for (int i = 0; i < whiteWords.size(); i++) {
        	String whiteWord = whiteWords.get(i).getWord();
        	//规则1：包含字符的都放行
        	if(content.contains(whiteWord))return FilterResult.GREEN;
        	
        	//其它规则 TODO
			
		}
        
        return FilterResult.YELLOW;
    }

    @Override
    public boolean sizeUp(Context context) {
        return false;
    }
}
