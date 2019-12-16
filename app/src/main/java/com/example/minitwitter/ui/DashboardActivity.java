package com.example.minitwitter.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.TweetFragment;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class DashboardActivity extends AppCompatActivity {

    private TweetFragment tweetFragment;
    private ExtendedFloatingActionButton fabCreate;
    private ImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboar);

        initFragments();
        setListeners();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/

        imgUser = findViewById(R.id.imgToolbarPhoto);
        setUserImage();
    }

    private void initFragments() {
        tweetFragment = new TweetFragment();
        fabCreate = findViewById(R.id.fabCreateTweet);

        setFragment(tweetFragment);
    }

    private void setListeners(){
        fabCreate.setOnClickListener(v -> {
            CreateTweetDialogFragment dialogFragment = new CreateTweetDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "CreateTweetDialogFragment");
        });
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
