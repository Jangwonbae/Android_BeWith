package com.example.bewith.util.location;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class DistanceCalculator {
    public static double calculateDistance(LatLng a, String b_lat, String b_lng){
        Location locationA = new Location("point A");//내위치
        locationA.setLatitude(a.latitude);
        locationA.setLongitude(a.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(Double.parseDouble(b_lat));
        locationB.setLongitude(Double.parseDouble(b_lng));

        double distance = locationA.distanceTo(locationB);
        return distance;
    }
}
