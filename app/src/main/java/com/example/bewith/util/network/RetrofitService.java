package com.example.bewith.util.network;

import com.example.bewith.data.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("getComment.php/")
    Call<Constants> retrofitGetComment();

    @FormUrlEncoded
    @POST("deleteComment.php")
    Call<Void> retrofitDeleteComment (@Field("id") String id);

    @FormUrlEncoded
    @POST("modifyComment.php")
    Call<Void> retrofitModifyComment (@Field("id") String id,
                                      @Field("category") String category,
                                      @Field("text") String text);
}