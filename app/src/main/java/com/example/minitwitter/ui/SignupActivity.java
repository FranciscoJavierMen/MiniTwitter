package com.example.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.MiniTwitterClient;
import com.example.minitwitter.retrofit.MiniTwitterService;
import com.example.minitwitter.retrofit.request.RequestSignup;
import com.example.minitwitter.retrofit.response.ResponseAuth;
import com.example.minitwitter.ui.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnSignUp;
    private TextView txtBackToLogin;
    private TextInputLayout edtName, edtEmail, edtPassword;
    private MiniTwitterClient client;
    private MiniTwitterService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        retrofitInit();
        initComponents();
        setListeners();
    }

    private void retrofitInit() {
        client = MiniTwitterClient.getInstance();
        service = client.getMiniTwitterService();
    }

    private void initComponents() {
        btnSignUp = findViewById(R.id.btnSignUp);
        txtBackToLogin = findViewById(R.id.txtSignUpToLogin);
        edtName = findViewById(R.id.edtNameSignUp);
        edtEmail = findViewById(R.id.edtEmailSignUp);
        edtPassword = findViewById(R.id.edtPasswordSignUp);
    }

    private void setListeners() {
        btnSignUp.setOnClickListener(this);
        txtBackToLogin.setOnClickListener(this);
    }

    private void goToLogin(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void goToSignUp() {
        String username = edtName.getEditText().getText().toString();
        String email = edtEmail.getEditText().getText().toString();
        String password = edtPassword.getEditText().getText().toString();
        String code = "UDEMYANDROID";

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else {
            RequestSignup requestSignup = new RequestSignup(username, email, password, code);
            Call<ResponseAuth> call = service.doSignup(requestSignup);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful() && response.body() != null){

                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTO_URL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ACTIVE, response.body().getActive());

                        Intent intent = new Intent(SignupActivity.this, DashboarActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignupActivity.this, "Registro realizado econ éxito. Bienvenido.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Ha ocurrido un problema inesperado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignupActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp:
                goToSignUp();
                break;
            case R.id.txtSignUpToLogin:
                goToLogin();
                break;
        }
    }


}
