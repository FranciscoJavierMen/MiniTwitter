package com.example.minitwitter.retrofit;

import com.example.minitwitter.RequestLogin;
import com.example.minitwitter.RequestSignup;
import com.example.minitwitter.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {

    @POST("/auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);

    @POST("/auth/login")
    Call<ResponseAuth> doSignup(@Body RequestSignup requestSignup);
}
