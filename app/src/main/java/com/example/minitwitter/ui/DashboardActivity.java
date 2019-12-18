package com.example.minitwitter.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.ProfileViewModel;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    private TweetFragment tweetFragment;
    private ExtendedFloatingActionButton fabCreate;
    private ImageView imgUser;
    private BottomNavigationView navigation;
    private TextView toolbar;
    private ProfileViewModel profileViewModel;

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

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
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

        /*profileViewModel.photoProfile.observe(this, photo -> {
            if (!photo.isEmpty()){
                Glide.with(this)
                        .load(Constantes.API_MINITWITTER_FILES_URL + photo)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imgUser);
            }
        });*/
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constantes.SELECT_PHOTO_GALLERY) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePath, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int imageIndex = cursor.getColumnIndex(filePath[0]);
                        String photoPath = cursor.getString(imageIndex);
                        profileViewModel.uploadPhoto(photoPath);
                        cursor.close();
                    }
                }
            }
        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constantes.SELECT_PHOTO_GALLERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        Toast.makeText(this, "No se puede acceder a la galería sin los permisos necesarios", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }
}
