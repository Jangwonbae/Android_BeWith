package com.example.bewith.view.community.data;

import com.google.gson.annotations.SerializedName;

public class ReplyData{
    @SerializedName("id")
    public int id;

    @SerializedName("replyId")
    public int ReplyId;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("UUID")
    public String ReplyUUID;

    @SerializedName("time")
    public String ReplyTime;

    @SerializedName("text")
    public String ReplyText;


    public ReplyData(int id,int ReplyId,String ReplyUUID,String ReplyTime,String nickname,String  ReplyText){
        this.id=id;
        this.ReplyId=ReplyId;
        this.ReplyUUID=ReplyUUID;
        this.ReplyTime=ReplyTime;
        this.nickname=nickname;
       this.ReplyText=ReplyText;
    }
}