package com.example.minitwitter.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.data.TweetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomModalFragment extends BottomSheetDialogFragment {

    private TweetViewModel tweetViewModel;
    private NavigationView navView;
    private int idTweetDelete;

    public static BottomModalFragment newInstance(int idTweet) {
        BottomModalFragment fragment = new BottomModalFragment();
        Bundle args = new Bundle();
        args.putInt(Constantes.ARGS_TWEET_ID, idTweet);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            idTweetDelete = getArguments().getInt(Constantes.ARGS_TWEET_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_modal_fragment, container, false);

        initComponents(view);
        setListeners();

        return view;
    }

    private void initComponents(View view) {
        navView = view.findViewById(R.id.navBottom);
    }

    private void setListeners() {
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_delete_tweet){
                tweetViewModel.deleteTweet(idTweetDelete);
                getDialog().dismiss();
            }
            return false;
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tweetViewModel = ViewModelProviders.of(this).get(TweetViewModel.class);
    }

}
