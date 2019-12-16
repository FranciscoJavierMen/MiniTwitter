package com.example.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.AuthTweeterService;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.request.RequestCreateTweet;
import com.example.minitwitter.retrofit.response.Like;
import com.example.minitwitter.retrofit.response.Tweet;
import com.example.minitwitter.retrofit.response.TweetDeleted;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.minitwitter.common.MyApp.getContext;

public class TweetRepository {
    private AuthTweeterService service;
    private AuthTwitterClient client;
    private MutableLiveData<List<Tweet>> allTweets;
    private MutableLiveData<List<Tweet>> favTweets;
    String userName;

    public TweetRepository() {
        client = AuthTwitterClient.getInstance();
        service = client.getAuthTwitterService();
        allTweets = getAllTweets();
        userName = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME);
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

    public MutableLiveData<List<Tweet>> getFavsTweets() {
        if (favTweets == null) {
            favTweets = new MutableLiveData<>();
        }

        List<Tweet> newFavList = new ArrayList<>();
        Iterator iterator = allTweets.getValue().iterator();
        while (iterator.hasNext()){
            Tweet current = (Tweet)iterator.next();
            Iterator itLikes = current.getLikes().iterator();
            boolean find = false;
            while (itLikes.hasNext() && !find){
                Like like = (Like)itLikes.next();
                if (like.getUsername().equals(userName)){
                    find = true;
                    newFavList.add(current);
                }
            }
        }

        favTweets.setValue(newFavList);

        return favTweets;
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

    public void likeTweet(int idTweet){
        Call<Tweet> call = service.likeTweet(idTweet);

        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful() && response.body() != null){
                    List<Tweet> listaClonada = new ArrayList<>();
                    //Añadir en primer lugar el nuevo tweet
                    for (int i = 0; i < allTweets.getValue().size(); i ++){
                        if (allTweets.getValue().get(i).getId() == idTweet){
                            listaClonada.add(response.body());
                        } else {
                            listaClonada.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }

                    allTweets.setValue(listaClonada);

                    getFavsTweets();
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

    public void deleteTweet(final int idTweet){
        Call<TweetDeleted> call = service.deleteTweet(idTweet);
        call.enqueue(new Callback<TweetDeleted>() {
            @Override
            public void onResponse(Call<TweetDeleted> call, Response<TweetDeleted> response) {
                if (response.isSuccessful() && response.body() != null){
                    List<Tweet> clonedTweets = new ArrayList<>();
                    for (int i = 0; i < allTweets.getValue().size(); i ++){
                        if (allTweets.getValue().get(i).getId() != idTweet){
                            clonedTweets.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }

                    allTweets.setValue(clonedTweets);
                    getFavsTweets();
                } else {
                    Toast.makeText(getContext(), "Ha ocurrido un prolema inesperado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TweetDeleted> call, Throwable t) {
                Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
