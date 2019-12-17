package com.example.minitwitter.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.ui.profile.ProfileFragment;
import com.example.minitwitter.ui.tweets.CreateTweetDialogFragment;
import com.example.minitwitter.ui.tweets.TweetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    private TweetFragment tweetFragment;
    private ExtendedFloatingActionButton fabCreate;
    private ImageView imgUser;
    private BottomNavigationView navigation;
    private TextView toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboar);

        toolbar = findViewById(R.id.txtTextToolbar);
        navigation = findViewById(R.id.nav_view);
        initFragments();
        setListeners();

        imgUser = findViewById(R.id.imgToolbarPhoto);
        setUserImage();
    }

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

        navigation.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.navigation_home:
                    toolbar.setText("Inicio");
                    fragment = TweetFragment.newInstance(Constantes.TWEET_LIST_ALL);
                    fabCreate.show();
                    break;
                case R.id.navigation_twitt_like:
                    toolbar.setText("Favoritos");
                    fragment = TweetFragment.newInstance(Constantes.TWEET_LIST_FAVS);
                    fabCreate.hide();
                    break;
                case R.id.navigation_account:
                    toolbar.setText("Cuenta");
                    fragment = new ProfileFragment();
                    fabCreate.hide();
                    break;
            }

            if (fragment != null){
                setFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void setUserImage(){
        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTO_URL);

        if (!TextUtils.isEmpty(photoUrl)){
            Glide.with(this)
                    .load(Constantes.API_MINITWITTER_FILES_URL.concat(photoUrl))
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imgUser);
        }
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {

    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        Toast.makeText(this, "No se puede acceder a la galería sin los permisos necesarios", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }
}
