package com.example.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.minitwitter.common.MyApp;
import com.example.minitwitter.retrofit.AuthTweeterService;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private AuthTweeterService service;
    private AuthTwitterClient client;
    private MutableLiveData<ResponseUserProfile> userProfile;

    public ProfileRepository() {
        client = AuthTwitterClient.getInstance();
        service = client.getAuthTwitterService();
        userProfile = getProfile();
    }

    public MutableLiveData<ResponseUserProfile> getProfile(){
        if (userProfile == null){
            userProfile = new MutableLiveData<>();
        }

        Call<ResponseUserProfile> call = service.getUserProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if (response.isSuccessful() && response.body() != null){
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();

            }
        });

        return userProfile;
    }
}
