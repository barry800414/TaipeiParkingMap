package com.socialgood.taipei.taipeidisablerparkingmap;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by ming on 2015/5/16.
 */
public class ParkingPlace {
    private int id, pkNum;
    private String type, address, time;
    private float lon, lat;

    public static ParkingPlace genParkingPlace(String dataLine, HashMap<String, Integer> colName){
        String[] entries = dataLine.split(",");
        if(entries.length != colName.size()){
            Log.d("ParkingPlace", "# entries inconsistent");
            return null;
        }
        ParkingPlace pp = new ParkingPlace();
        for(String col: colName.keySet()){
            int index = (Integer) colName.get(col);
            if(col == "OBJECTID"){
                pp.setId(Integer.parseInt(entries[index]));
            }
            else if(col == "PKTYPE1"){
                pp.setType(entries[index]);
            }
            else if(col == "PKTIME"){
                pp.setTime(entries[index]);
            }
            else if(col == "address"){
                pp.setAddress(entries[index]);
            }
            else if(col == "PK#"){
                pp.setPkNum(Integer.parseInt(entries[index]));
            }
            else if(col == "gps_x"){
                pp.setLon(Float.parseFloat(entries[index]));
            }
            else if(col == "gps_y"){
                pp.setLat(Float.parseFloat(entries[index]));
            }
        }
        return pp;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPkNum() {
        return pkNum;
    }

    public void setPkNum(int pkNum) {
        this.pkNum = pkNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
