package com.example.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.minitwitter.retrofit.AuthTweeterService;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.minitwitter.common.MyApp.getContext;

public class TweetRepository {
    private AuthTweeterService service;
    private AuthTwitterClient client;
    private LiveData<List<Tweet>> allTweets;

    public TweetRepository() {
        client = AuthTwitterClient.getInstance();
        service = client.getAuthTwitterService();
        allTweets = getAllTweets();
    }

    private LiveData<List<Tweet>> getAllTweets(){
        final MutableLiveData<List<Tweet>> data = new MutableLiveData<>();

        Call<List<Tweet>> call = service.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful() && response.body() != null){
                    data.setValue(response.body());
                } else {
                    Toast.makeText(getContext(), "Error inesperado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        return data;
    }
}
