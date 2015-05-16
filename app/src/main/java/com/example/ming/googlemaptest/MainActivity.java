package com.example.ming.googlemaptest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button downloadButton = null;
    private Button readButtion = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = (Button) this.findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(dBOnClickListener);

        readButtion = (Button) this.findViewById(R.id.readButton);
        readButtion.setOnClickListener(rBOnClickListener);

        Log.d("main", this.getPackageResourcePath());
    }


    private Button.OnClickListener dBOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            Log.d("File Download", "click to download");
            Downloader dl = new Downloader(view.getContext());
            dl.execute("http://140.112.187.33/~r02922010/TaipeiParkMap/park01_new.csv", "park01_new.csv");
        }
    };

    private Button.OnClickListener rBOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            ParkingDataReader pdr = new ParkingDataReader(view.getContext());
            pdr.read("park01_new.csv");
        }
    };


}