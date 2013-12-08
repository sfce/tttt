package us.cmcc.sms.cleaner.filter;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午2:50
 */
public enum FilterResult {
    /**
     * 拦截
     */
    RED,
    /**
     * 放行
     */
    GREEN,
    /**
     * 当前Filter处理不了，由下一下Filter处理
     */
    YELLOW
}
