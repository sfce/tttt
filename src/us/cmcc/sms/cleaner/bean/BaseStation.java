package us.cmcc.sms.cleaner.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-12-10
 * Time: 下午8:42
 */
public class BaseStation implements Serializable {
    private String name;
    private int lac;
    private ArrayList<Integer> cids;
    private double latitude;
    private double longitude;
    private Area area;
    private boolean state;

    public BaseStation() {
    }

    public BaseStation(String name, int lac, ArrayList<Integer> cids, double latitude, double longitude) {
        this.name = name;
        this.lac = lac;
        this.cids = cids;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public ArrayList<Integer> getCids() {
        return cids;
    }

    public void setCids(ArrayList<Integer> cids) {
        this.cids = cids;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
