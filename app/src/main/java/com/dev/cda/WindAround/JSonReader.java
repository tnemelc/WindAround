package com.dev.cda.WindAround;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by daviller on 16-Jun-16.
 */
public class JSonReader {
    static protected final String TAG = "jsonReader";

    static public String loadFromJson(InputStreamReader rd) throws JSONException, IOException {
        StringBuilder strB = new StringBuilder();
        int len;
        char buffer[] = new char[1024 * 16];
        while(rd.ready()) {
            len = rd.read(buffer);
            strB.append(buffer, 0, len);
        }
        return strB.toString();
    }

    static public Vector<Beacon> parseBeaconList(JSONObject obj) {
        //InputStreamReader dataFile = new InputStreamReader(jsonFile);
        Vector <Beacon> beaconList = new Vector<>();
        try {
            //JSONObject obj = new JSONObject(jsonData);
            //JSONObject obj = new JSONObject("{\"doc\": \"plip\", \"license\": \"plop\", \"attribution\": \"plup\"}");
            JSONArray dataArray = obj.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObj = dataArray.getJSONObject(i);
                String strId = dataObj.getString("id");
                //metadata
                JSONObject metaObj = dataObj.getJSONObject("meta");
                String name = metaObj.getString("name");
                //location
                JSONObject locationObj = dataObj.getJSONObject("location");
                String longitude = locationObj.getString("longitude");
                String latitude = locationObj.getString("latitude");
                String date = locationObj.getString("date");
                String succes = locationObj.getString("success");

                JSONObject measObj = dataObj.getJSONObject("measurements");
                String measDate = measObj.getString("date");
                String pressure = measObj.getString("pressure");
                String wind_heading = measObj.getString("wind_heading");
                String wind_speed_avg = measObj.getString("wind_speed_avg");
                String wind_speed_max = measObj.getString("wind_speed_max");
                String wind_speed_min = measObj.getString("wind_speed_min");

                JSONObject statusObj = dataObj.getJSONObject("status");
                String statusDate = statusObj.getString("date");
                String snr = statusObj.getString("date");
                String state = statusObj.getString("date");

                /*double x = Double.parseDouble(jo_inside.getString("posX"));
                double y =  Double.parseDouble(jo_inside.getString("posY"));*/
                //int id = Integer.parseInt(strId);
                String windDir;
                if (wind_heading != "null")
                    windDir = AngleToOrientation(Double.parseDouble(wind_heading));
                else
                    windDir ="null";
                double lat = Double.parseDouble(longitude);
                double lon = Double.parseDouble(latitude);

                beaconList.add(new Beacon(name, wind_speed_avg, wind_speed_max, wind_speed_min,
                        wind_heading, windDir, measDate, strId, lat, lon));
            }
        } catch (JSONException e){
            Log.i(TAG, "JSON Data error");
            return null;
        }
        return beaconList;
    }

    static public  Beacon parseBeacon(JSONObject obj) {
        try {
            JSONObject dataObj = obj.getJSONObject("data");
            String strId = dataObj.getString("id");
            //metadata
            JSONObject metaObj = dataObj.getJSONObject("meta");
            String name = metaObj.getString("name");
            //location
            JSONObject locationObj = dataObj.getJSONObject("location");
            String longitude = locationObj.getString("longitude");
            String latitude = locationObj.getString("latitude");
            String date = locationObj.getString("date");
            String succes = locationObj.getString("success");

            JSONObject measObj = dataObj.getJSONObject("measurements");
            String measDate = measObj.getString("date");
            String pressure = measObj.getString("pressure");
            String wind_heading = measObj.getString("wind_heading");
            String wind_speed_avg = measObj.getString("wind_speed_avg");
            String wind_speed_max = measObj.getString("wind_speed_max");
            String wind_speed_min = measObj.getString("wind_speed_min");

            JSONObject statusObj = dataObj.getJSONObject("status");
            String statusDate = statusObj.getString("date");
            String snr = statusObj.getString("date");
            String state = statusObj.getString("date");

                /*double x = Double.parseDouble(jo_inside.getString("posX"));
                double y =  Double.parseDouble(jo_inside.getString("posY"));*/
            //int id = Integer.parseInt(strId);
            String windDir;
            if (wind_heading != "null")
                windDir = AngleToOrientation(Double.parseDouble(wind_heading));
            else
                windDir ="null";
            double lat = Double.parseDouble(longitude);
            double lon = Double.parseDouble(latitude);

            return new Beacon(name, wind_speed_avg, wind_speed_max, wind_speed_min, wind_heading, windDir, measDate, strId, lat, lon);
        } catch (JSONException e){
            Log.i(TAG, "JSON Data error");
            return null;
        }
    }
    static private String AngleToOrientation(double angle) {
        angle -= 15;
        if (angle < 15)
            return "N";
        else if (angle < 45)
            return "NNE";
        else if (angle < 75)
            return "ENE";
        else if (angle < 105)
            return "E";
        else if (angle < 135)
            return "ESE";
        else if (angle < 165)
            return "SSE";
        else if (angle < 195)
            return "S";
        else if (angle < 225)
            return "SSW";
        else if (angle < 255)
            return "WSW";
        else if (angle < 285)
            return "W";
        else if (angle < 315)
            return "WNW";
        else
            return "NNW";
    }
}

