package com.example.minitwitter.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DashboardActivity extends AppCompatActivity {

    private TweetFragment tweetFragment;
    private ExtendedFloatingActionButton fabCreate;
    private ImageView imgUser;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboar);

        initFragments();
        setListeners();

        navigation = findViewById(R.id.nav_view);

        imgUser = findViewById(R.id.imgToolbarPhoto);
        setUserImage();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.navigation_home:
                    fragment = TweetFragment.newInstance(Constantes.TWEET_LIST_ALL);
                    break;
                case R.id.navigation_twitt_like:
                    fragment = TweetFragment.newInstance(Constantes.TWEET_LIST_FAVS);
                    break;
            }

            if (fragment != null){
                setFragment(fragment);
                return true;
            }
            return false;
        }
    };

    private void initFragments() {
        tweetFragment = TweetFragment.newInstance(Constantes.TWEET_LIST_ALL);
        fabCreate = findViewById(R.id.fabCreateTweet);

        setFragment(tweetFragment);
    }

    private void setListeners(){
        fabCreate.setOnClickListener(v -> {
            CreateTweetDialogFragment dialogFragment = new CreateTweetDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "CreateTweetDialogFragment");
        });

        navigation.setOnNavigationItemSelectedListener(mListener);
    }

    private void setUserImage(){
        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTO_URL);

        if (!TextUtils.isEmpty(photoUrl)){
            Glide.with(this)
                    .load(Constantes.API_MINITWITTER_FILES_URL.concat(photoUrl))
                    .into(imgUser);
        }
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }


}
