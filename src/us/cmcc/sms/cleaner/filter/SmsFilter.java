package us.cmcc.sms.cleaner.filter;

import android.content.Context;
import us.cmcc.sms.cleaner.bean.Sms;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午5:25
 */
public abstract class SmsFilter implements ISmsFilter {
    private ISmsFilter next;

    public SmsFilter(ISmsFilter next) {
        setNext(next);
    }

    @Override
    public void setNext(ISmsFilter filter) {
        this.next = filter;
    }

    @Override
    public boolean dispatch(Context context, Sms sms) {
        if (sizeUp(context)) {
            FilterResult result = doFilter(context, sms);
            switch (result) {
                case RED: return true;
                case GREEN: return false;
                case YELLOW:
                    if (null != next) {
                        return next.dispatch(context, sms);
                    }
                    break;
            }
        } else {
            if (null != next) {
                return next.dispatch(context, sms);
            }
        }
        return false;
    }
}
