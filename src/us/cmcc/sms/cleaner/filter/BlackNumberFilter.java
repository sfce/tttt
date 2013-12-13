package us.cmcc.sms.cleaner.filter;

import java.util.List;
import java.util.regex.Pattern;

import us.cmcc.sms.cleaner.MyApplication;
import us.cmcc.sms.cleaner.bean.Sms;
import us.cmcc.sms.cleaner.dao.BlackNumber;
import us.cmcc.sms.cleaner.dao.BlackNumberDao;
import us.cmcc.sms.cleaner.dao.DaoMaster;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午6:10
 */
public class BlackNumberFilter extends SmsFilter {
    public BlackNumberFilter(ISmsFilter next) {
        super(next);
    }

    @Override
    public FilterResult doFilter(Context context, Sms sms) {
    	DaoMaster.DevOpenHelper dbOpenHelper = new DaoMaster.DevOpenHelper(context, MyApplication.DB_NAME, null);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        BlackNumberDao dao = daoMaster.newSession().getBlackNumberDao();
        List<BlackNumber> blackNumbers = dao.loadAll();
        
        String number = sms.getNumber();
        for (int i = 0; i < blackNumbers.size(); i++) {
        	String blackNumber = blackNumbers.get(i).getNumber();
        	//规则1：以*结尾的,代表以XX开头   比如139*，代表139开头的所有号码都过滤
        	if(number.endsWith("*")){
        		if(Pattern.compile(number).matcher(blackNumber.substring(0, blackNumber.length()-1) + ".*").matches()) return FilterResult.RED; 
        	}
        	
        	//规则2： 包含号码段的
        	if(number.contains(blackNumber))return FilterResult.RED;
        	
        	//其它规则 TODO
			
		}
        
        return FilterResult.YELLOW;
    }

    @Override
    public boolean sizeUp(Context context) {
        return false;
    }
}
