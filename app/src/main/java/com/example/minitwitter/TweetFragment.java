package com.example.minitwitter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.minitwitter.retrofit.AuthTweeterService;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetFragment extends Fragment {

    private int mColumnCount = 1;
    private RecyclerView recyclerTweets;
    private MyTweetRecyclerViewAdapter adapter;
    private List<Tweet> tweetList;
    private AuthTweeterService service;
    private AuthTwitterClient client;

    public TweetFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerTweets = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerTweets.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerTweets.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            retrofitInit();
           loadTweetData();
        }
        return view;
    }

    private void retrofitInit() {
        client = AuthTwitterClient.getInstance();
        service = client.getAuthTwitterService();
    }

    private void loadTweetData() {
        Call<List<Tweet>> call = service.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful() && response.body() != null){
                    tweetList = response.body();
                    adapter = new MyTweetRecyclerViewAdapter(
                            getActivity(),
                            tweetList
                    );
                    recyclerTweets.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error inesperado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
