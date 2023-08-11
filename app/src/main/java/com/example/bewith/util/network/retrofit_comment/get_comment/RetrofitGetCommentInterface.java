package com.example.bewith.util.network.retrofit_comment.get_comment;

import com.example.bewith.util.network.retrofit_comment.get_comment.PostGetCommentResult;

import retrofit2.Call;
import retrofit2.http.POST;

public interface RetrofitGetCommentInterface {
    // @GET( EndPoint-자원위치(URI) )
    @POST("getComment.php/")
    Call<PostGetCommentResult> retrofitGetComment();

}