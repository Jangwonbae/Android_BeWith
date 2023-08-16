package com.example.bewith.view.community.data;

import com.google.gson.annotations.SerializedName;

public class LikeModel {
    @SerializedName("UUID")
    public String UUID;

    public LikeModel(String UUID){
        this.UUID=UUID;
    }
}
