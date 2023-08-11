package com.example.bewith.util.network.retrofit_comment.get_comment;

import com.example.bewith.view.main.data.CommentData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostGetCommentResult {
    @SerializedName("comment")
    private ArrayList<CommentData> comment;

    public ArrayList<CommentData>  getCommentArrayList() {
        return comment;
    }
}
