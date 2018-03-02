package com.example.humraz.rithack;

/**
 * Created by humra on 3/1/2018.
 */
public class ada {
    private String lat;
    private String longg;
    private String count;
    private String flag;


    long stackId;
    public ada() {
      /*Blank default constructor essential for Firebase*/
    }
    public ada(String a)
    {

    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongg() {
        return longg;
    }

    public void setLongg(String longg) {
        this.longg = longg;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}