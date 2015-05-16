package com.example.ming.googlemaptest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ming on 2015/5/15.
 */
public class Downloader extends AsyncTask {

    private static final int BUFFER_SIZE = 4096;
    private Context context;

    public Downloader(Context context){
        this.context = context;
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

    protected void onPostExecute(String result){

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
