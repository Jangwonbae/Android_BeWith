package com.example.bewith.view.search_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.bewith.R;
import com.example.bewith.view.search_map.data.OfficeArrayListClass;
import com.example.bewith.view.search_map.data.OfficeData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class SearchMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private String where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        where = intent.getStringExtra("Where");
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        for(OfficeData officeData : OfficeArrayListClass.officeDataArrayList){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(officeData.getOfficeLatLng());
            String markerTitle;
            if(where.equals("office")){
                markerTitle=officeData.getOfficeName();
            }
            else{
                markerTitle=officeData.getOfficeName()+" 화장실";
            }
            markerOptions.title(markerTitle);
            mMap.addMarker(markerOptions);
        }

        mMap.setOnMarkerClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.212711,126.952116), 17));


    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        marker.showInfoWindow();
        AlertDialog.Builder ad = new AlertDialog.Builder(SearchMapActivity.this);
        ad.setTitle("길안내");
        ad.setMessage(marker.getTitle() +"(으)로 AR네이게이션을 실행합니다.");

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SearchMapActivity.this, UnityPlayerActivity.class);
                UnityPlayer.UnitySendMessage("ButtonManager", "StartRoute", marker.getTitle());
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        ad.show();

        return true;
    }
}
