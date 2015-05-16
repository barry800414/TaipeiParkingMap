package com.socialgood.taipei.taipeidisablerparkingmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by stanleyliao on 15/5/15.
 */
public class ParkingPlaceDrawer {

    public static void drawOne(ParkingPlace parkingPlace, GoogleMap mMap){
        mMap.addMarker(new MarkerOptions().title("類型："+parkingPlace.getType()).snippet("地址："+parkingPlace.getAddress()+"\n車位數量："+parkingPlace.getPkNum()+"\n停車時間："+parkingPlace.getTime()).position(new LatLng(parkingPlace.getLat(),parkingPlace.getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)).anchor(0.5f,0.5f));
    }

    public static void drawAll(ParkingPlace[] parkingList, GoogleMap mMap){
        for(int i=0; i<parkingList.length; i++){
            drawOne(parkingList[i],mMap);
        }
    }

    public static void drawAll(ArrayList<ParkingPlace> parkingList, GoogleMap mMap){
        for(int i=0; i<parkingList.size(); i++){
            drawOne(parkingList.get(i),mMap);
        }
    }
}
