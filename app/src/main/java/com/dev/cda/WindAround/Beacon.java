package com.dev.cda.WindAround;

import com.google.android.gms.maps.model.LatLng;
/**
 * Created by daviller on 16-Jun-16.
 */
public class Beacon {
    public final String mName;
    public final String mWindSpeed;
    public final String mWindSpeedMax;
    public final String mWindSpeedMin;
    public final String mOrientation;
    public final String mDirection;
    public final String mMeasDate;
    public final String mId;
    public final LatLng mLocation;


    public Beacon(String name, String windSpeedAvg, String windSpeedMax, String windSpeedMin,
                  String orientation, String direction, String measDate, String id,
                  double latitude, double longitude) {
        mName = name;
        mWindSpeed = windSpeedAvg;
        mWindSpeedMax = windSpeedMax;
        mWindSpeedMin = windSpeedMin;
        mOrientation = orientation;
        mDirection = direction;
        mMeasDate = measDate;
        mId = id;
        mLocation = new LatLng(longitude, latitude);

    }
    String getName() {return mName;}
    LatLng getLocation() {return mLocation;}

    public String getWindSpeedMax() {
        return mWindSpeedMax;
    }

    public String getWindSpeedMin() {
        return mWindSpeedMin;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }
    public String getOrientation() {
        return mOrientation;
    }
    public String getDirection() {
        return mDirection;
    }
    public String getMeasDate() {
        return mMeasDate;
    }
}
