package com.example.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.minitwitter.R;
import com.example.minitwitter.ui.MainActivity;
import com.google.android.material.button.MaterialButton;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnSignUp;
    private TextView txtBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initComponents();
        setListeners();
    }

    private void initComponents() {
        btnSignUp = findViewById(R.id.btnSignUp);
        txtBackToLogin = findViewById(R.id.txtSignUpToLogin);
    }

    private void setListeners() {
        btnSignUp.setOnClickListener(this);
        txtBackToLogin.setOnClickListener(this);
    }

    private void goToLogin(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp:
                break;
            case R.id.txtSignUpToLogin:
                goToLogin();
                break;
        }
    }
}
