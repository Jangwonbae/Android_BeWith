package com.example.bewith.view.main.util.map_item;

import com.example.bewith.view.main.data.CommentData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerCreator {
    public void addMarker(GoogleMap mMap, CommentData commentData){
        LatLng latLng = new LatLng(Double.parseDouble(commentData.latitude), Double.parseDouble(commentData.logitude));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(commentData.text);
        markerOptions.snippet(commentData.time);

        mMap.addMarker(markerOptions);
    }
}
