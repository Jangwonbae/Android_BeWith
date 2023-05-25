package com.example.bewith.listclass;

public class ReplyData{
    public int id;
    public int ReplyId;
    public String nickname;
    public String ReplyUUID;
    public String ReplyTime;
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