package com.example.bewith.view.main.activity;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.bewith.util.location.DistanceCalculator;
import com.example.bewith.util.network.RetrofitResult;
import com.example.bewith.util.network.RetrofitService;
import com.example.bewith.view.main.data.CommentData;
import com.example.bewith.data.Constants;
import com.google.android.gms.maps.model.LatLng;
import com.rugovit.eventlivedata.MutableEventLiveData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityViewModel extends ViewModel {
    private String UUID;
    private String IP_ADDRESS;
    private MutableEventLiveData<ArrayList<CommentData>> commentArrayListLiveData;//전체 코멘트 정보
    private MutableEventLiveData<ArrayList<CommentData>> spinnerCommentArrayListLiveData;//스피너 목록에 따라 보여지는 코멘트 정보

    private Retrofit retrofit;
    private RetrofitService retrofitService;

    public MainActivityViewModel() {
        UUID = Constants.UUID;
        IP_ADDRESS = Constants.IP_ADDRESS;

        retrofit = new Retrofit.Builder()
                .baseUrl("http://" +IP_ADDRESS+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

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

        //retrofit을 통한 객체 구현, 추상 메소드 중 사용할 메소드 Call 객체에 등록
        Call<RetrofitResult> call = retrofitService.retrofitGetComment();
        //비동기 enqueue 작업으로 실행, 통신종료 후 이벤트 처리를 위해 Callback 등록
        call.enqueue(new Callback<RetrofitResult>() {
            @Override
            public void onResponse(Call<RetrofitResult> call, Response<RetrofitResult> response) {
                if(response.isSuccessful()){
                    RetrofitResult result = response.body();
                    ArrayList tempCommentArrayList = new ArrayList();

                    for (CommentData commentData : result.getCommentArrayList()) {
                        double distance = DistanceCalculator.calculateDistance(new LatLng(MainActivity.myLatitude, MainActivity.myLongitude),
                                commentData.latitude, commentData.longitude);//두점 사이 계산
                        if (distance < 1000) {//전체 데이터중 거리가 1km이하인 데이터만 저장함
                            tempCommentArrayList.add(commentData);
                        }
                    }
                    getCommentArrayListLiveData();
                    commentArrayListLiveData.setValue(tempCommentArrayList);
                    onSeleteSpinner(radiusIndex);

                }
                else{
                    Log.d("MainActivityViewModelGet","실패");
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult> call, Throwable t) {
                Log.d("MainActivityViewModelGet",t.toString());
            }
        });

    }

    public void onSeleteSpinner(int radiusIndex) {
        ArrayList<CommentData> tempSpinnerCommentArrayList = new ArrayList();
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
                    double distance = DistanceCalculator.calculateDistance(new LatLng(MainActivity.myLatitude, MainActivity.myLongitude),
                            commentData.latitude, commentData.longitude);//두점 사이 계산
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
    public void deleteComment(int radiusIndex, String _id) {
        Call<Void> call = retrofitService.retrofitDeleteComment(_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    getComment(radiusIndex);
                }
                else{
                    Log.d("MainActivityViewModelDelete","실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("MainActivityViewModelDelete",t.toString());
            }
        });

    }
    public void modifyComment(int radiusIndex, String _id, String category, String text){
        Call<Void> call = retrofitService.retrofitModifyComment(_id, category, text);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    getComment(radiusIndex);
                    Log.d("MainActivityViewModelModify",_id+"성공"+category+text+"@@@@@@@@@@");
                }
                else{
                    Log.d("MainActivityViewModelModify","실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("MainActivityViewModelModify",t.toString());
            }
        });
    }
}


