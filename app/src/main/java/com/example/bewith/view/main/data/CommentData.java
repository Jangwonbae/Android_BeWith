package com.example.bewith.view.main.data;

import com.google.gson.annotations.SerializedName;

public class CommentData {
    @SerializedName("id")
    public int _id;

    @SerializedName("UUID")
    public String UUID;

    @SerializedName("time")
    public String time;

    @SerializedName("category")
    public String category;

    @SerializedName("text")
    public String text;

    @SerializedName("str_latitude")
    public String latitude;

    @SerializedName("str_longitude")
    public String longitude;

    public CommentData(int _id,String UUID,String time, String category,String text,String latitude,String longitude){
        this._id=_id;
        this.UUID=UUID;
        this.time=time;
        this.category=category;
        this.text=text;
        this.latitude=latitude;
        this.longitude=longitude;

    }
}