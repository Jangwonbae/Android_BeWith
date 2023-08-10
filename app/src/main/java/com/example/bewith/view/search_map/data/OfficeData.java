package com.example.bewith.view.search_map.data;

import com.google.android.gms.maps.model.LatLng;

public class OfficeData {
    private String officeName;
    private LatLng officeLatLng;
    public OfficeData(String officeName, LatLng officeLatLng){
        this.officeName = officeName;
        this.officeLatLng = officeLatLng;
    }

    public String getOfficeName() {
        return officeName;
    }

    public LatLng getOfficeLatLng() {
        return officeLatLng;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public void setOfficeLatLng(LatLng officeLatLng) {
        this.officeLatLng = officeLatLng;
    }
}
