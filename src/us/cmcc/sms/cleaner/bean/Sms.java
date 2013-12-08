package us.cmcc.sms.cleaner.bean;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-8
 * Time: 下午2:40
 */
public class Sms {
    private String number;
    private String body;
    private long time;

    public Sms() {
    }

    public Sms(String number, String body, long time) {
        this.number = number;
        this.body = body;
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
