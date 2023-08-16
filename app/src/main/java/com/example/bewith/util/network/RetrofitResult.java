package com.example.bewith.util.network;

import com.example.bewith.view.community.data.LikeModel;
import com.example.bewith.view.community.data.ReplyData;
import com.example.bewith.view.main.data.CommentData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetrofitResult {
    @SerializedName("comment")
    private ArrayList<CommentData> comment;

    @SerializedName("reply")
    private ArrayList<ReplyData> reply;//댓글 정보

    @SerializedName("likes")
    private ArrayList<LikeModel> like;//좋아요 정보

    public ArrayList<CommentData>  getCommentArrayList() {
        return comment;
    }

    public ArrayList<ReplyData>  getReplyArrayList() {
        return reply;
    }

    public ArrayList<LikeModel> getLike() {
        return like;
    }
}
