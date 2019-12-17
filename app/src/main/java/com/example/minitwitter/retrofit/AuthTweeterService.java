package com.example.minitwitter.retrofit;

import com.example.minitwitter.retrofit.request.RequestCreateTweet;
import com.example.minitwitter.retrofit.request.RequestUserProfile;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;
import com.example.minitwitter.retrofit.response.Tweet;
import com.example.minitwitter.retrofit.response.TweetDeleted;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthTweeterService {

    //Tweets
    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    @POST("tweets/create")
    Call<Tweet> createTweet(@Body RequestCreateTweet request);

    @POST("tweets/like/{id}")
    Call<Tweet> likeTweet(@Path("id") int idTweet);

    @DELETE("tweets/{id}")
    Call<TweetDeleted> deleteTweet(@Path("id") int idTweet);

    //Users
    @GET("users/profile")
    Call<ResponseUserProfile> getUserProfile();

    @PUT("users/profile")
    Call<ResponseUserProfile> updateUserProfile(@Body RequestUserProfile request);
}
