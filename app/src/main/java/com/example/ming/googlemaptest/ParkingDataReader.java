package com.example.ming.googlemaptest;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ming on 2015/5/15.
 */
public class ParkingDataReader {
    private Context context;
    public ParkingDataReader(Context context){
        this.context = context;
    }
    public ArrayList<ParkingPlace> read(String fileName){
        try {
            InputStream is = context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String buf = null;
            int lineNum = 0;
            HashMap<String, Integer> colName = null;
            ArrayList<ParkingPlace> ppList = new ArrayList<ParkingPlace>();

            while( (buf = reader.readLine()) != null){
                Log.d("Data Reader", buf);
                if(lineNum == 0){
                    colName = genColNameMap(buf);
                }
                else{
                    ParkingPlace pp = ParkingPlace.genParkingPlace(buf, colName);
                    if(pp != null){
                        ppList.add(pp);
                    }
                }
            }
            return ppList;
        }
        catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.d("Data Reader", errors.toString());
            Log.d("Data Reader", "reading csv data failed");
        }
    }

    private HashMap<String, Integer> genColNameMap(String firstLine){
        HashMap<String, Integer> colName = new HashMap<String, Integer>();
        String[] entries = firstLine.split(",");
        for(int i = 0; i < entries.length; i++){
            colName.put(entries[i].trim(), i);
        }
        return colName;
    }
}
