package com.example.bewith.util.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("getComment.php/")
    Call<RetrofitResult
            > retrofitGetComment();

    @FormUrlEncoded
    @POST("deleteComment.php")
    Call<Void> retrofitDeleteComment(@Field("id") String id);

    @FormUrlEncoded
    @POST("modifyComment.php")
    Call<Void> retrofitModifyComment(@Field("id") String id,
                                     @Field("category") String category,
                                     @Field("text") String text);

    @FormUrlEncoded
    @POST("getReply.php")
    Call<RetrofitResult> retrofitGetReply(@Field("replyId") String replyId);

    @FormUrlEncoded
    @POST("getLike.php")
    Call<RetrofitResult> retrofitGetLike(@Field("replyId") String replyId);

    @FormUrlEncoded
    @POST("addReply.php")
    Call<Void> retrofitAddReply(@Field("UUID") String UUID,
                                @Field("replyId") String replyId,
                                @Field("nickname") String nickname,
                                @Field("text") String text);

    @FormUrlEncoded
    @POST("deleteReply.php")
    Call<Void> retrofitDeleteReply(@Field("id") String id);

    @FormUrlEncoded
    @POST("addLike.php")
    Call<Void> retrofitAddLike(@Field("replyId") String replyId,
                               @Field("UUID") String UUID);

    @FormUrlEncoded
    @POST("deleteLike.php")
    Call<Void> retrofitDeleteLike(@Field("replyId") String replyId,
                                  @Field("UUID") String UUID);

    @FormUrlEncoded
    @POST("ModifyReply.php")
    Call<Void> retrofitModifyReply(@Field("id") String id,
                                  @Field("text") String text);
}