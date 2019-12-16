package com.example.minitwitter.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.TweetViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class CreateTweetDialogFragment extends DialogFragment {
    public CreateTweetDialogFragment() { }

    private MaterialToolbar toolbar;
    private MaterialButton btnCreateTweet;
    private EditText edtTweet;
    private ImageView imgUser;
    private TweetViewModel tweetViewModel;

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
        setUserImage();
        setListeners();
        return view;
    }

    private void initComponents(View view) {
        tweetViewModel = ViewModelProviders.of(getActivity())
                .get(TweetViewModel.class);

        toolbar = view.findViewById(R.id.toolbarCreateTweet);
        btnCreateTweet = view.findViewById(R.id.btnNewTweet);
        edtTweet = view.findViewById(R.id.edtNewTweet);
        imgUser = view.findViewById(R.id.imgUser);
    }

    private void setUserImage(){
        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTO_URL);
        if (!TextUtils.isEmpty(photoUrl)){
            Glide.with(getActivity())
                    .load(Constantes.API_MINITWITTER_FILES_URL + photoUrl)
                    .into(imgUser);
        }
    }

    private void setToolbar() {
        toolbar.setTitle("Nuevo tweet");
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> {
            String mensaje = edtTweet.getText().toString();
            if (!TextUtils.isEmpty(mensaje)){
                showConfirmDialog();
            } else {
                getDialog().dismiss();
            }
        });
    }

    private void setListeners() {
        btnCreateTweet.setOnClickListener(v -> {
            String mensaje = edtTweet.getText().toString();
            if (TextUtils.isEmpty(mensaje)){
                Toast.makeText(getContext(), "No puedes publicar tweets vacios", Toast.LENGTH_SHORT).show();
            } else {
                createTweet(mensaje);
                getDialog().dismiss();
            }
        });
    }

    private void showConfirmDialog(){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        dialogBuilder.setTitle("¿Desea cancelar el tweet?, El tweet será eliminado.")
                .setMessage("")
                .setPositiveButton("Tweettear", (dialog, which) -> {
                    dialog.dismiss();
                    getDialog().dismiss();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .create()
        .show();
    }

    private void createTweet(String mensaje){
        tweetViewModel.newTweet(mensaje);
    }
}
