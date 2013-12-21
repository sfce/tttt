package us.cmcc.sms.cleaner.util;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-15
 * Time: 下午7:32
 */
public interface LocationListener {
    void onLocationResult(boolean success, String location);
}
