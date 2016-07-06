package com.dev.cda.WindAround;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class BeaconActivity extends AppCompatActivity{
    //update wakeup service subscript
    private static final String mTAG = "BroadcastTest";
    private Intent mIntent;

    Beacon mBcn;
    TableLayout mll;
    String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        Bundle b = getIntent().getBundleExtra("beacon");
        if (null == b) {
            Toast.makeText(this.getBaseContext(), " error getting bundle", Toast.LENGTH_LONG).show();
            return;
        }
        //String bcnName = b.getString("name");
        String url;
        try {
            mUrl = "http://api.pioupiou.fr/v1/live/" + b.getString("BcnId");
        }catch (Exception e) {
            Toast.makeText(this.getBaseContext(), " error getting BcnId", Toast.LENGTH_LONG).show();
            return;
        }
        mIntent = new Intent(this, JSonRequestBeaconService.class);
        //updateUI();
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
        if (id == R.id.about_us) {
            Intent intent = new Intent();
            intent.setClass(this.getBaseContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        this.startService(mIntent);
        registerReceiver(broadcastReceiver, new IntentFilter(JSonRequestBeaconService.BROADCAST_ACTION));
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(mIntent);
    }

    private void updateUI(){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mBcn = JSonReader.parseBeacon(response);
                        } catch (Exception e) {
                            Toast.makeText(BeaconActivity.this.getBaseContext(),
                                    " error parsing Beacon JSon: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (null == mBcn) {
                            return;
                        }
                        setUI();
                        /*Toast.makeText(BeaconActivity.this.getBaseContext(),
                                "UI is updated ",
                                Toast.LENGTH_SHORT).show();*/
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

    protected void setUI(){
        mll = (TableLayout) findViewById(R.id.displayBecon);
        mll.removeAllViewsInLayout();
        TableRow titleRow = new TableRow(this);
        titleRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        TextView tvTitle = new TextView(this);
        tvTitle.setTextSize(20);
        tvTitle.setTextColor(Color.DKGRAY);
        tvTitle.setText(mBcn.getName());
        titleRow.addView(tvTitle);
        mll.addView(titleRow);

        mll.addView(createTableRow("Wind speed: ", mBcn.getWindSpeed() + "km/h"));
        mll.addView(createTableRow("Wind speed Max: ", mBcn.getWindSpeedMax() + "km/h"));
        mll.addView(createTableRow("Wind speed Min: ", mBcn.getWindSpeedMin()+ "km/h"));
        //mll.addView(createTableRow("Wind direction: ", mBcn.getOrientation()+ "°"));
        mll.addView(createTableRow("Wind direction: ", mBcn.getDirection()));
        mll.addView(createTableRow("Last meas. date: ", ""));
        mll.addView(createTableRow(mBcn.getMeasDate(), ""));
       //TableRow addRow = createTableRow("plus");
        //addRow.setOnTouchListener(createAddListener(addRow));
        //mll.addView(addRow);

        return;
    }

    private TableRow createTableRow(String text, String value){
        TableRow row = new TableRow(this);
        row.setPadding(0, (int) (getHeight() * 0.025), 0, 0);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        TextView  leftCol = createTextView(text);
        row.addView(leftCol);
        TextView  rightCol = createTextView(value);
        row.addView(rightCol);
        return row;
    }
    private TableRow createTableRow(String path){
        TableRow row = new TableRow(this);
        row.setPadding(0, (int) (getHeight() * 0.025), 0, 0);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView  col = createPicView(path);
        row.addView(col);
        return row;
    }

    private TextView createTextView(String text){
        TextView tv = new TextView(this);
        tv.setTextSize(20);
        tv.setTextColor(Color.DKGRAY);
        tv.setText(text);
        return tv;
    }

    protected TextView createPicView(String picName){
        TextView pv = new TextView(this);
        int picId = getResources().getIdentifier(picName,
                "drawable", getPackageName());
        if (picId != 0) {
            Drawable d = ImageResizer.resize(getDrawable(picId),
                    getWidth() / 5 , getWidth() / 5, getResources());
            pv.setBackground(d);
        } else {
            pv.setText("add");
        }
        pv.setGravity(Gravity.CENTER);
        //pv.setLayoutParams(lpPic);
        return pv;
    }

    protected int getWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    protected int getHeight(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
    protected View.OnTouchListener createAddListener(final TableRow ContentRow){
        return new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int evtAction = event.getAction();
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    Intent intent = new Intent();
                    intent.setClass(BeaconActivity.this, MapsActivity.class);
                    BeaconActivity.this.startActivity(intent);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_SCROLL)
                    return true;
                return false;
            }
        };
    }
}
