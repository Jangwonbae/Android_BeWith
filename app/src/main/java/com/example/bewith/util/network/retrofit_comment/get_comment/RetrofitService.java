package com.example.bewith.util.network.retrofit_comment.get_comment;

import com.example.bewith.util.network.retrofit_comment.get_comment.PostGetCommentResult;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {
    @POST("getComment.php/")
    Call<PostGetCommentResult> retrofitGetComment();

    @FormUrlEncoded
    @POST("deleteComment.php")
    Call<Void> retrofitDeleteComment (@Field("id") String id);
}