package com.example.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minitwitter.R;
import com.example.minitwitter.retrofit.MiniTwitterClient;
import com.example.minitwitter.retrofit.MiniTwitterService;
import com.example.minitwitter.retrofit.request.RequestLogin;
import com.example.minitwitter.retrofit.response.ResponseAuth;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnLogin;
    private TextView txtGoToSignUp;
    private TextInputLayout edtEmail, edtPassword;
    private MiniTwitterClient client;
    private MiniTwitterService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofifInit();
        initComponents();
        setListeners();
    }

    private void retrofifInit() {
        client = MiniTwitterClient.getInstance();
        service = client.getMiniTwitterService();
    }

    private void initComponents() {
        btnLogin = findViewById(R.id.btnLogin);
        txtGoToSignUp = findViewById(R.id.txtSignLoginToSignUp);
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
    }

    private void setListeners(){
        btnLogin.setOnClickListener(this);

        txtGoToSignUp.setOnClickListener(this);
    }

    private void goToSignUp() {
        startActivity(new Intent(this, SignupActivity.class));
        finish();
    }

    private void goToLogin() {
        String email = edtEmail.getEditText().getText().toString();
        String password = edtPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)){
            edtEmail.setError("El email es requerido");
        } else if(TextUtils.isEmpty(password)){
            edtPassword.setError("La contraseña es requerida");
        } else {
            RequestLogin requestLogin = new RequestLogin(email, password);

            Call<ResponseAuth> call = service.doLogin(requestLogin);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful() && response.body() != null){
                        Toast.makeText(MainActivity.this, "Sesión iniciada con éxito", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, DashboarActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                goToLogin();
                break;
            case R.id.txtSignLoginToSignUp:
                goToSignUp();
                break;
        }
    }




}
