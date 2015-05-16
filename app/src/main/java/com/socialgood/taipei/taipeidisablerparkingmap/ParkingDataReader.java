package com.socialgood.taipei.taipeidisablerparkingmap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ming on 2015/5/15.
 */
public class ParkingDataReader extends AsyncTask{
    private Context context = null;
    private String resLink = null;
    private TextView msgTextView = null;
    private GoogleMap mMap;
    private int trialNum = 0;
    private static final int MAX_TRIAL = 5;
    private static final String FILENAME = "parking.csv";
    private static final int BUFFER_SIZE = 4096;

    public ParkingDataReader(Context context, String resLink, GoogleMap mMap){
        this.context = context;
        this.resLink = resLink;
        this.mMap = mMap;
        this.trialNum = 0;
    }

    public ParkingDataReader(Context context, String resLink, GoogleMap mMap, TextView megTextView){
        this(context, resLink, mMap);
        this.msgTextView = msgTextView;
    }

    public ArrayList<ParkingPlace> loadAndDraw(){
        try {
            InputStream is = context.openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String buf = null;
            int lineNum = 0;
            HashMap<String, Integer> colName = null;
            ArrayList<ParkingPlace> ppList = new ArrayList<ParkingPlace>();

            while( (buf = reader.readLine()) != null){
                if(lineNum == 0){
                    Log.d("Data Reader", buf);
                    colName = genColNameMap(buf);
                }
                else{
                    ParkingPlace pp = ParkingPlace.genParkingPlace(buf, colName);
                    if(pp != null){
                        ppList.add(pp);
                    }
                }
                lineNum = lineNum + 1;
            }
            Log.d("Data Reader", "Start drawing");
            ParkingPlaceDrawer.drawAll(ppList, mMap);
            return ppList;
        }
        catch(FileNotFoundException e){
            if(trialNum >= MAX_TRIAL){
                Log.d("Data Reader", "Reach the maximum trial time");
            }
            this.execute(this.resLink, FILENAME);
            trialNum += 1;
            return null;
        }
        catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.d("Data Reader", errors.toString());
            Log.d("Data Reader", "reading csv data failed");
            return null;
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



    protected void onPreExecute(){

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        if(!checkInternetConnection()){
            Log.d("File Download", "No Internet available");
            return false;
        }
        if(objects.length != 2){
            Log.d("File Download", "Input arguments are incorrect");
            return false;
        }
        else{
            String link = (String) objects[0];
            String fileName = (String) objects[1];
            Log.d("File Download", "link:" + link + " filename:" + fileName);
            return downloadFile(link, fileName);
        }
    }

    protected void onPostExecute(boolean result){
        if(result) {
            if (this.msgTextView != null) {
                msgTextView.setText("下載停車位資訊成功");
            }
            loadAndDraw();
        }
    }

    //check Internet connection
    private boolean checkInternetConnection(){
        Log.d("File Download", "int check");
        ConnectivityManager check = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(check != null){
            NetworkInfo[] info = check.getAllNetworkInfo();
            if(info != null){
                for(int i=0;i <info.length; i++) {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.d("File Download", "Internet is connected");
                        return true;
                    }
                }
            }
        }
        else{
            Log.d("File Download", "Not connected to Internet");
        }
        return false;
    }

    public boolean downloadFile(String link, String fileName){
        Log.d("File Download", "in download");
        try{
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true); //?
            conn.connect();
            InputStream is = conn.getInputStream();
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            Log.d("File Download", "Download files success!");
            is.close();
            fos.close();
            return true;
        }
        catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.d("File Download", errors.toString());
            Log.d("File Download", "Download files failed");
            return false;
        }
    }



}
