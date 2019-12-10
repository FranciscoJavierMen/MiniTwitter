package com.example.minitwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnLogin;
    private TextView txtGoToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        setListeners();
    }

    private void initComponents() {
        btnLogin = findViewById(R.id.btnLogin);
        txtGoToSignUp = findViewById(R.id.txtSignLoginToSignUp);
    }

    private void setListeners(){
        btnLogin.setOnClickListener(this);

        txtGoToSignUp.setOnClickListener(this);
    }

    private void goToSignUp() {
        startActivity(new Intent(this, SignupActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                break;
            case R.id.txtSignLoginToSignUp:
                goToSignUp();
                break;
        }
    }


}