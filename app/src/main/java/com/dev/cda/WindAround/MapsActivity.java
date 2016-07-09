package com.dev.cda.WindAround;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Vector;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //private static final LatLng SYDNEY = new LatLng(-33.85704, 151.21522);
    private Vector<Beacon> mBeaconList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng chambery = new LatLng(45.56460099999999, 5.917780999999991);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Chambery"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chambery, 5));
        loadBeaconData();

    }

    public void loadBeaconData() {
        String url = "http://api.pioupiou.fr/v1/live/all";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mBeaconList = JSonReader.parseBeaconList(response);
                        if (null == mBeaconList) {
                            return;
                        }
                        try {
                            for (Iterator<Beacon> it = mBeaconList.iterator(); it.hasNext(); ) {
                                final Beacon bcn = it.next();
                                MarkerOptions bcnMarker = new MarkerOptions().position(bcn.getLocation()).title(bcn.getName() +
                                        "\n windSpeed: " + bcn.getWindSpeed() + "km/h");
                                bcnMarker.title(bcn.mId);
                                mMap.addMarker(bcnMarker);
                                mMap.setOnMarkerClickListener(
                                        new GoogleMap.OnMarkerClickListener(){
                                        @Override
                                        public boolean onMarkerClick(Marker arg0){
                                            //Toast.makeText(MapsActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                                            Bundle b = new Bundle();
                                            b.putString("BcnId", arg0.getTitle());
                                            Intent intent = new Intent();
                                            intent.setClass(MapsActivity.this, BeaconActivity.class);
                                            intent.putExtra("beacon", b);
                                            MapsActivity.this.startActivity(intent);
                                            return true;
                                        }
                                        }

                                );
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                            }
                            Toast.makeText(MapsActivity.this, "Touch any marker to get beacon information", Toast.LENGTH_LONG).show();
                        } catch(Exception e){
                            return;
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }
}
