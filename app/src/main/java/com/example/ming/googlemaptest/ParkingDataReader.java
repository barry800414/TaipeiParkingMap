package com.example.ming.googlemaptest;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by ming on 2015/5/15.
 */
public class ParkingDataReader {
    private Context context;
    public ParkingDataReader(Context context){
        this.context = context;
    }
    public void read(String fileName){
        try {
            InputStream is = context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String buf = null;
            while( (buf = reader.readLine()) != null){
                Log.d("Data Reader", buf);
            }

        }
        catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.d("Data Reader", errors.toString());
            Log.d("Data Reader", "reading csv data failed");
        }
    }
}
