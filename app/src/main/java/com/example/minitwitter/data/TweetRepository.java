package com.example.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.minitwitter.retrofit.AuthTweeterService;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.request.RequestCreateTweet;
import com.example.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.minitwitter.common.MyApp.getContext;

public class TweetRepository {
    private AuthTweeterService service;
    private AuthTwitterClient client;
    private MutableLiveData<List<Tweet>> allTweets;

    public TweetRepository() {
        client = AuthTwitterClient.getInstance();
        service = client.getAuthTwitterService();
        allTweets = getAllTweets();
    }

    public MutableLiveData<List<Tweet>> getAllTweets(){
        if (allTweets == null){
            allTweets = new MutableLiveData<>();
        }

        Call<List<Tweet>> call = service.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful() && response.body() != null){
                    allTweets.setValue(response.body());
                } else {
                    Toast.makeText(getContext(), "Error inesperado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        return allTweets;
    }

    public void createTweet(String mensaje){
        RequestCreateTweet request = new RequestCreateTweet(mensaje);
        Call<Tweet> call = service.createTweet(request);

        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful() && response.body() != null){
                    List<Tweet> listaClonada = new ArrayList<>();
                    //Añadir en primer lugar el nuevo tweet
                    listaClonada.add(response.body());
                    for (int i = 0; i < allTweets.getValue().size(); i ++){
                        listaClonada.add(new Tweet(allTweets.getValue().get(i)));
                    }

                    allTweets.setValue(listaClonada);
                } else {
                    Toast.makeText(getContext(), "Algo ha salido mal, intente de nuevo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
