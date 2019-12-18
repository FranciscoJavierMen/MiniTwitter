package com.example.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.MyApp;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.AuthTweeterService;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.request.RequestUserProfile;
import com.example.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private AuthTweeterService service;
    private AuthTwitterClient client;
    private MutableLiveData<ResponseUserProfile> userProfile;
    private MutableLiveData<String> photoProfile;

    public ProfileRepository() {
        client = AuthTwitterClient.getInstance();
        service = client.getAuthTwitterService();
        userProfile = getProfile();
        if (photoProfile == null){
            photoProfile = new MutableLiveData<>();
        }
    }

    //Método para obtener los datos del usuario
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

    //Método para actualizar los datos del usuario
    public void updateProfile(RequestUserProfile request){
        Call<ResponseUserProfile> call = service.updateUserProfile(request);
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if (response.isSuccessful() && response.body() != null){
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void uploadPhoto(String photoPath){
        File file = new File(photoPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        Call<ResponseUploadPhoto> call = service.uploadProfilePhoto(requestBody);
        call.enqueue(new Callback<ResponseUploadPhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
                if (response.isSuccessful() && response.body() != null){
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTO_URL, response.body().getFilename());
                    photoProfile.setValue(response.body().getFilename());
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTO_URL, response.body().getFilename());
                    Toast.makeText(MyApp.getContext(), "Foto de perfil actualizada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyApp.getContext(), "Ha ocurrido un error al intentar subir la foto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<String> getPhotoProfile(){
        return photoProfile;
    }
}
