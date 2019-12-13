package com.example.minitwitter.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboar);

        initFragments();
        setListeners();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/

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

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }


}
