package com.example.minitwitter.retrofit;

import com.example.minitwitter.common.Constantes;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {

    private static AuthTwitterClient instance = null;
    private AuthTweeterService miniTwitterService;
    private Retrofit retrofit;

    public AuthTwitterClient(){
        OkHttpClient.Builder OkHttpClientBuilder = new OkHttpClient.Builder();
        OkHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient client = OkHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        miniTwitterService = retrofit.create(AuthTweeterService.class);
    }

    //Patrón singleton
    public static AuthTwitterClient getInstance(){
        if (instance == null){
            instance = new AuthTwitterClient();
        }

        return instance;
    }

    public AuthTweeterService getAuthTwitterService(){
        return miniTwitterService;
    }
}
