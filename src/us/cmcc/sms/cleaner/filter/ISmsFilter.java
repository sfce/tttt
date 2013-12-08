package us.cmcc.sms.cleaner.filter;

import android.content.Context;
import us.cmcc.sms.cleaner.bean.Sms;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午2:38
 */
public interface ISmsFilter {
    FilterResult doFilter(Sms sms);

    void setNext(ISmsFilter filter);

    /**
     * @param context
     * @param sms
     * @return true:需要过滤; false:不需要过滤
     */
    boolean dispatch(Context context, Sms sms);

    boolean sizeUp(Context context);
}
