package com.example.minitwitter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.minitwitter.data.TweetViewModel;
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
    private TweetViewModel tweetViewModel;

    public TweetFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = ViewModelProviders.of(getActivity())
                .get(TweetViewModel.class);
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

            adapter = new MyTweetRecyclerViewAdapter(
                    getActivity(),
                    tweetList
            );
            recyclerTweets.setAdapter(adapter);

           loadTweetData();
        }
        return view;
    }


    private void loadTweetData() {
        tweetViewModel.getTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                adapter.setData(tweets);
            }
        });
    }
}
