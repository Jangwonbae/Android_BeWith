package com.example.bewith.view.community.activity;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.bewith.data.Constants;
import com.example.bewith.util.network.RetrofitResult;
import com.example.bewith.util.network.RetrofitService;
import com.example.bewith.view.community.data.LikeData;
import com.example.bewith.view.community.data.LikeModel;
import com.example.bewith.view.community.data.ReplyData;
import com.rugovit.eventlivedata.MutableEventLiveData;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommunityActivityViewModel extends ViewModel {
    private String IP_ADDRESS;
    private String UUID;
    private MutableEventLiveData<LikeData> likeDataLiveData;
    private MutableEventLiveData<ArrayList<ReplyData>> replyDataArrayListLiveData;

    private Retrofit retrofit;
    private RetrofitService retrofitService;

    public CommunityActivityViewModel() {
        IP_ADDRESS = Constants.IP_ADDRESS;
        UUID=Constants.UUID;

        retrofit = new Retrofit.Builder()
                .baseUrl("http://" +IP_ADDRESS+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
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
        //retrofit을 통한 객체 구현, 추상 메소드 중 사용할 메소드 Call 객체에 등록
        Call<RetrofitResult> call = retrofitService.retrofitGetLike(communityId);
        //비동기 enqueue 작업으로 실행, 통신종료 후 이벤트 처리를 위해 Callback 등록
        call.enqueue(new Callback<RetrofitResult>() {
            @Override
            public void onResponse(Call<RetrofitResult> call, Response<RetrofitResult> response) {
                if(response.isSuccessful()){
                    RetrofitResult result = response.body();
                    LikeData likeData = new LikeData();
                    if(!result.getLike().isEmpty()){
                        likeData.setLikeCount(result.getLike().size());
                        for(LikeModel likeModel : result.getLike()){
                            if(likeModel.UUID.equals(UUID)){
                                likeData.setMyLike(true);
                            }
                        }
                    }
                    getLikeDataLiveData();
                    likeDataLiveData.setValue(likeData);

                }
                else{
                    Log.d("CommunityActivityViewModelGet","실패");
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult> call, Throwable t) {
                Log.d("CommunityActivityViewModelGet",t.toString());
            }
        });
    }

    public void getReplyData(String communityId) {
        //retrofit을 통한 객체 구현, 추상 메소드 중 사용할 메소드 Call 객체에 등록
        Call<RetrofitResult> call = retrofitService.retrofitGetReply(communityId);
        //비동기 enqueue 작업으로 실행, 통신종료 후 이벤트 처리를 위해 Callback 등록
        call.enqueue(new Callback<RetrofitResult>() {
            @Override
            public void onResponse(Call<RetrofitResult> call, Response<RetrofitResult> response) {
                if(response.isSuccessful()){
                    RetrofitResult result = response.body();
                    getLikeData(communityId);
                    getReplyDataArrayListLiveData();
                    replyDataArrayListLiveData.setValue(result.getReplyArrayList());

                }
                else{
                    Log.d("CommunityActivityViewModelGet","실패");
                }
            }

            @Override
            public void onFailure(Call<RetrofitResult> call, Throwable t) {
                Log.d("CommunityActivityViewModelGet",t.toString());
            }
        });

    }
    public void addReplyData(String communityId, String name, String text){
        //retrofit을 통한 객체 구현, 추상 메소드 중 사용할 메소드 Call 객체에 등록
        Call<Void> call = retrofitService.retrofitAddReply(UUID, communityId, name, text);
        //비동기 enqueue 작업으로 실행, 통신종료 후 이벤트 처리를 위해 Callback 등록
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    getReplyData(communityId);
                }
                else{
                    Log.d("CommunityActivityViewModelAddReply","실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("CommunityActivityViewModelAddReply",t.toString());
            }
        });
    }
    public void deleteReplyData(String communityId, String _id){
        //retrofit을 통한 객체 구현, 추상 메소드 중 사용할 메소드 Call 객체에 등록
        Call<Void> call = retrofitService.retrofitDeleteReply(_id);
        //비동기 enqueue 작업으로 실행, 통신종료 후 이벤트 처리를 위해 Callback 등록
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    getReplyData(communityId);
                }
                else{
                    Log.d("CommunityActivityViewModelDeleteReply","실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("CommunityActivityViewModelDeleteReply",t.toString());
            }
        });
    }
    public void modifyReply(String communityId, String id, String text){
        //retrofit을 통한 객체 구현, 추상 메소드 중 사용할 메소드 Call 객체에 등록
        Call<Void> call = retrofitService.retrofitModifyReply(id,text);
        //비동기 enqueue 작업으로 실행, 통신종료 후 이벤트 처리를 위해 Callback 등록
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    getReplyData(communityId);
                }
                else{
                    Log.d("CommunityActivityViewModelModifyReply","실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("CommunityActivityViewModelModifyReply",t.toString());
            }
        });
    }
    public void controlLikeData(Boolean like, String replyId){

        Call<Void> call;
        if(like){
            //retrofit을 통한 객체 구현, 추상 메소드 중 사용할 메소드 Call 객체에 등록
            call = retrofitService.retrofitAddLike(replyId, UUID);
        }
        else{
            call = retrofitService.retrofitDeleteLike(replyId, UUID);
        }
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                }
                else{
                    Log.d("CommunityActivityViewModelAddLike","실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("CommunityActivityViewModelDeleteLike",t.toString());
            }
        });
    }

}
