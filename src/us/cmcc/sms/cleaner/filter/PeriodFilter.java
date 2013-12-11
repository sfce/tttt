package us.cmcc.sms.cleaner.filter;

import android.content.Context;
import us.cmcc.sms.cleaner.bean.Sms;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午6:08
 */
public class PeriodFilter extends SmsFilter {
    public PeriodFilter(ISmsFilter next) {
        super(next);
    }

    @Override
    public FilterResult doFilter(Context context, Sms sms) {
        return null;
    }

    @Override
    public boolean sizeUp(Context context) {
        return false;
    }
}
