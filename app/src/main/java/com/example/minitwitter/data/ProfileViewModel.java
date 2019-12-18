package com.example.minitwitter.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.minitwitter.retrofit.request.RequestUserProfile;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;

import org.jetbrains.annotations.NotNull;

public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository profileRepository;
    public LiveData<ResponseUserProfile> userProfile;
    public LiveData<String> photoProfile;

    public ProfileViewModel(@NotNull Application application) {
        super(application);
        profileRepository = new ProfileRepository();
        userProfile = profileRepository.getProfile();
        photoProfile = profileRepository.getPhotoProfile();
    }

    public void updateProfile(RequestUserProfile request){
        profileRepository.updateProfile(request);
    }

    public void uploadPhoto(String photoPath){
        profileRepository.uploadPhoto(photoPath);
    }
}
