package com.example.minitwitter.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.data.TweetViewModel;
import com.example.minitwitter.retrofit.response.Tweet;
import com.example.minitwitter.ui.MyTweetRecyclerViewAdapter;

import java.util.List;

public class TweetFragment extends Fragment {
    private int tweetListType = 1;
    private RecyclerView recyclerTweets;
    private MyTweetRecyclerViewAdapter adapter;
    private List<Tweet> tweetList;
    private TweetViewModel tweetViewModel;
    private SwipeRefreshLayout refreshLayout;

    public TweetFragment() { }

    public static TweetFragment newInstance(int tweetListType){
        TweetFragment fragment = new TweetFragment();
        Bundle args = new Bundle();
        args.putInt(Constantes.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = ViewModelProviders.of(getActivity())
                .get(TweetViewModel.class);

        if (getArguments() != null){
            tweetListType = getArguments().getInt(Constantes.TWEET_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        refreshLayout = view.findViewById(R.id.swipeRefreshTweets);
        recyclerTweets = view.findViewById(R.id.list);

        Context context = view.getContext();
        refreshTweets();

        recyclerTweets.setLayoutManager(new LinearLayoutManager(context));

        adapter = new MyTweetRecyclerViewAdapter(
                getActivity(),
                tweetList
        );
        recyclerTweets.setAdapter(adapter);

        if (tweetListType == Constantes.TWEET_LIST_ALL){
            loadTweetData();
        } else if (tweetListType == Constantes.TWEET_LIST_FAVS){
            loadFavoriteTweetdata();
        }

        return view;
    }

    private void loadFavoriteTweetdata() {
    }

    private void refreshTweets() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.blueColor),
                getResources().getColor(R.color.colorPink));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (tweetListType == Constantes.TWEET_LIST_ALL){
                    loadNewTweetData();
                } else if (tweetListType == Constantes.TWEET_LIST_FAVS){
                    loadNewFavoriteTweetdata();
                }
            }
        });
    }

    private void loadNewFavoriteTweetdata() {
    }


    private void loadTweetData() {
        tweetViewModel.getTweets().observe(getActivity(), tweets -> {
            tweetList = tweets;
            adapter.setData(tweetList);
        });
    }

    private void loadNewTweetData() {
        tweetViewModel.getNewTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                refreshLayout.setRefreshing(false);
                adapter.setData(tweetList);
                tweetViewModel.getNewTweets().removeObserver(this);
            }
        });
    }
}
