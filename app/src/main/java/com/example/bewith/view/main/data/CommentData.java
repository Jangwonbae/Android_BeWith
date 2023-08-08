package com.example.bewith.view.main.data;

public class CommentData {
    public int _id;
    public String UUID;
    public String time;
    public int category;
    public String text;
    public String latitude;
    public String logitude;

    public CommentData(int _id,String UUID,String time, int category,String text,String latitude,String logitude){
        this._id=_id;
        this.UUID=UUID;
        this.time=time;
        this.category=category;
        this.text=text;
        this.latitude=latitude;
        this.logitude=logitude;

    }
}