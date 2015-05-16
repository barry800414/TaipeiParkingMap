package com.socialgood.taipei.taipeidisablerparkingmap;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends ActionBarActivity {

    private GoogleMap mMap;
    private MapFragment mMapFragment;
    private TextView msgTextView;
    private boolean mbIsZoomFirst = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        msgTextView = (TextView) findViewById(R.id.messageText);
        mMapFragment = new MapFragment(){
            public void onActivityCreated(Bundle saveInstanceState){
                super.onActivityCreated(savedInstanceState);
                mMap = mMapFragment.getMap();
                setUpMap();
            }
        };

        //start drawing map
        msgTextView.setText("載入地圖...");
        getFragmentManager().beginTransaction().add(R.id.frameLayMapContainer, mMapFragment).commit();

        //start downloading data
        msgTextView.setText("載入停車位資訊...");
        ParkingDataReader dr = new ParkingDataReader(this, "http://140.112.187.33/~r02922010/TaipeiParkMap/park01_new.csv", mMap, msgTextView);
        dr.loadAndDraw();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpMap(){
        mMap.setMyLocationEnabled(true);
        UiSettings mapUi = mMap.getUiSettings();
        mapUi.setZoomControlsEnabled(false);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.map_info_window, null);
                TextView txtTitle = (TextView)v.findViewById(R.id.txtTitle);
                txtTitle.setText(marker.getTitle());
                TextView txtSpippet = (TextView)v.findViewById(R.id.txtSpippet);
                txtSpippet.setText(marker.getSnippet());
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
            public void onInfoWindowClick(Marker marker){
                marker.hideInfoWindow();
            }
        });
    }

}