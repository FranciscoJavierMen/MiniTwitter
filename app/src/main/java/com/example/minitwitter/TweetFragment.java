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

import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class TweetFragment extends Fragment {

    private int mColumnCount = 1;
    private RecyclerView recyclerTweets;
    private MyTweetRecyclerViewAdapter adapter;
    private List<Tweet> tweetList;

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

           loadTweetData();
        }
        return view;
    }

    private void loadTweetData() {
        adapter = new MyTweetRecyclerViewAdapter(
                getActivity(),
                tweetList
        );
        recyclerTweets.setAdapter(adapter);
    }


}
