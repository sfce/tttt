package us.cmcc.sms.cleaner.filter;

import android.content.Context;
import us.cmcc.sms.cleaner.bean.Sms;

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
    public FilterResult doFilter(Sms sms) {
        return null;
    }

    @Override
    public boolean sizeUp(Context context) {
        return false;
    }
}
