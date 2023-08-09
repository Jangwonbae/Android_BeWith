package com.example.bewith.view.community.activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bewith.R;
import com.example.bewith.data.Constants;
import com.example.bewith.util.network.community.AddReply;
import com.example.bewith.util.network.community.ControlLike;
import com.example.bewith.util.network.community.DeleteReply;
import com.example.bewith.util.network.community.GetLike;
import com.example.bewith.util.network.community.GetReply;
import com.example.bewith.view.community.data.LikeData;
import com.example.bewith.view.community.data.ReplyData;
import com.example.bewith.view.main.data.CommentData;
import com.rugovit.eventlivedata.MutableEventLiveData;

import java.util.ArrayList;

public class CommunityActivityViewModel extends ViewModel {
    private String IP_ADDRESS;
    private MutableEventLiveData<LikeData> likeDataLiveData;
    private MutableEventLiveData<ArrayList<ReplyData>> replyDataArrayListLiveData;

    public CommunityActivityViewModel() {
        IP_ADDRESS = Constants.IP_ADDRESS;
    }

    public MutableEventLiveData<LikeData> getLikeDataLiveData() {
        if (likeDataLiveData == null) {
            likeDataLiveData = new MutableEventLiveData<LikeData>();
        }
        return likeDataLiveData;
    }
    public MutableEventLiveData<ArrayList<ReplyData>> getReplyDataArrayListLiveData() {
        if (replyDataArrayListLiveData == null) {
            replyDataArrayListLiveData = new MutableEventLiveData<ArrayList<ReplyData>>();
        }
        return replyDataArrayListLiveData;

    }
    public void getLikeData(String communityId) {//좋아요 데이터 받아오기
        //동기 처리
        GetLike getLike = new GetLike();
        getLike.execute("http://" + IP_ADDRESS + "/getLike.php", communityId);
        //동기 처리 되어야 함
        getLikeDataLiveData();
        likeDataLiveData.setValue(Constants.likeData);
    }

    public void getReplyData(String communityId) {
        //동기 처리
        GetReply getReply = new GetReply();
        getReply.execute("http://" + IP_ADDRESS + "/getReply.php", communityId);
        //동기 처리 되어야 함
        getLikeData(communityId);
        getReplyDataArrayListLiveData();
        replyDataArrayListLiveData.setValue(Constants.replyDataArrayList);
    }
    public void addReplyData(String commuityUUID, String communityId, String name, String text){
        //동기 처리
        AddReply addReply = new AddReply();//
        addReply.execute("http://" + IP_ADDRESS + "/addReply.php", commuityUUID, communityId, name, text);//서버에 전송
        //동기 처리 되어야 함
        getReplyData(communityId);

    }
    public void deleteReplyData(String communityId, String replyId){
        //동기 처리
        DeleteReply deleteReply = new DeleteReply();//내 comment 내용 서버에 전송
        deleteReply.execute("http://" + IP_ADDRESS + "/deleteReply.php", replyId);//서버에 전송
        //동기 처리 되어야 함
        getReplyData(communityId);
    }
    public void controlLikeData(Boolean like, String commuityUUID, String communityId){
        ControlLike controlLike = new ControlLike();//
        if(like){
            controlLike.execute("http://" + IP_ADDRESS + "/addLike.php", communityId, commuityUUID);//서버에 전송
        }
        else{
            controlLike.execute("http://" + IP_ADDRESS + "/deleteLike.php", communityId, commuityUUID);//서버에 전송
        }
    }
}
