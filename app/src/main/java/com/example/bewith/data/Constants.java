package com.example.bewith.data;

import com.example.bewith.view.community.data.LikeData;
import com.example.bewith.view.community.data.ReplyData;
import com.example.bewith.view.main.data.CommentData;
import com.example.bewith.view.search_map.data.OfficeData;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
    public static String UUID;
    public static final String IP_ADDRESS = "221.147.144.65:80";

    public static ArrayList<ReplyData> replyDataArrayList = new ArrayList<>();//댓글 정보
    public static LikeData likeData = new LikeData();

    @SerializedName("comment")
    private ArrayList<CommentData> comment;

    public ArrayList<CommentData>  getCommentArrayList() {
        return comment;
    }
}
