package com.example.bewith.view.main.activity;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.bewith.util.location.DistanceCalculator;
import com.example.bewith.view.main.data.CommentData;
import com.example.bewith.util.network.comment.GetComment;
import com.example.bewith.data.Constants;
import com.google.android.gms.maps.model.LatLng;
import com.rugovit.eventlivedata.MutableEventLiveData;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {
    private String UUID;
    private String IP_ADDRESS;
    private MutableEventLiveData<ArrayList<CommentData>> commentArrayListLiveData;//전체 코멘트 정보
    private MutableEventLiveData<ArrayList<CommentData>> spinnerCommentArrayListLiveData;//스피너 목록에 따라 보여지는 코멘트 정보


    public MainActivityViewModel() {
        UUID = Constants.UUID;
        IP_ADDRESS = Constants.IP_ADDRESS;
    }

    public MutableEventLiveData<ArrayList<CommentData>> getCommentArrayListLiveData() {
        if (commentArrayListLiveData == null) {
            commentArrayListLiveData = new MutableEventLiveData<ArrayList<CommentData>>();
        }
        return commentArrayListLiveData;
    }

    public MutableEventLiveData<ArrayList<CommentData>> getSpinnerCommentArrayListLiveData() {
        if (spinnerCommentArrayListLiveData == null) {
            spinnerCommentArrayListLiveData = new MutableEventLiveData<ArrayList<CommentData>>();
        }
        return spinnerCommentArrayListLiveData;
    }

    public void getComment(int radiusIndex) {
        //동기처리 해야함
        GetComment getComment = new GetComment();
        getComment.execute("http://" + IP_ADDRESS + "/getComment.php", "");
        //받은게 완료된 후 진행되야함


        ArrayList tempCommentArrayList = new ArrayList();

        for (CommentData commentData : Constants.commnentDataArrayList) {
            double distance = DistanceCalculator.calculateDistance(new LatLng(MainActivity.myLatitude, MainActivity.myLogitude),
                    commentData.latitude, commentData.logitude);//두점 사이 계산
            if (distance < 1000) {//전체 데이터중 거리가 1km이하인 데이터만 저장함
                tempCommentArrayList.add(commentData);
            }
        }
        getCommentArrayListLiveData();
        commentArrayListLiveData.setValue(tempCommentArrayList);
        onSeleteSpinner(radiusIndex);
    }

    public void onSeleteSpinner(int radiusIndex) {
        ArrayList tempSpinnerCommentArrayList = new ArrayList();
        int m = 0;
        try{
            for (CommentData commentData : getCommentArrayListLiveData().getValue()) {
                if (radiusIndex == 0) {//스피너가 my comment면
                    if (commentData.UUID.equals(UUID)) {//내가 생성한 코멘트를 넘음
                        tempSpinnerCommentArrayList.add(commentData);
                    }
                } else {
                    switch (radiusIndex) {
                        case 1:
                            m = 30;
                            break;
                        case 2:
                            m = 100;
                            break;
                        case 3:
                            m = 300;
                            break;
                        case 4:
                            m = 500;
                            break;
                        case 5:
                            m = 1000;
                            break;
                    }
                    double distance = DistanceCalculator.calculateDistance(new LatLng(MainActivity.myLatitude, MainActivity.myLogitude),
                            commentData.latitude, commentData.logitude);//두점 사이 계산
                    if (distance < m) {
                        tempSpinnerCommentArrayList.add(commentData);
                    }
                }
            }
            getSpinnerCommentArrayListLiveData();
            spinnerCommentArrayListLiveData.setValue(tempSpinnerCommentArrayList);
        }catch (Exception e){
            Log.e("Error",e.toString());
        }

    }
}


