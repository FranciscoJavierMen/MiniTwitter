package com.example.minitwitter.ui;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minitwitter.R;
import com.google.android.material.appbar.MaterialToolbar;


public class CreateTweetDialogFragment extends DialogFragment {

    public CreateTweetDialogFragment() {
    }

    private MaterialToolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_tweet_dialog, container, false);

        initComponents(view);
        setToolbar();
        setListeners();
        return view;
    }

    private void setToolbar() {
        toolbar.setTitle("Nuevo tweet");
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> dismiss());
    }

    private void setListeners() {

    }

    private void initComponents(View view) {
        toolbar = view.findViewById(R.id.toolbarCreateTweet);
    }

}
