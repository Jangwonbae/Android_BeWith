package com.example.bewith.view.main.activity;

import androidx.lifecycle.ViewModel;

import com.example.bewith.view.main.data.CommentData;
import com.example.bewith.util.network.GetComment;
import com.example.bewith.view.main.data.Constants;
import com.rugovit.eventlivedata.MutableEventLiveData;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {
    private String IP_ADDRESS;
    private MutableEventLiveData<ArrayList<CommentData>> CommentArrayListLiveData;//전체 코멘트 정보

    public MainActivityViewModel(){
        IP_ADDRESS=Constants.IP_ADDRESS;
    }
    public MutableEventLiveData<ArrayList<CommentData>> getCommentList(){
        if (CommentArrayListLiveData == null){
            CommentArrayListLiveData= new MutableEventLiveData<ArrayList<CommentData>>();
        }
        return CommentArrayListLiveData;
    }
    public void getComment(){
        //동기처리 해야함
        GetComment getComment = new GetComment();
        getComment.execute( "http://" + IP_ADDRESS + "/getComment.php", "");
        //받은게 완료된 후 진행되야함
        CommentArrayListLiveData.setValue(Constants.commnentDataArrayList);

    }
}
