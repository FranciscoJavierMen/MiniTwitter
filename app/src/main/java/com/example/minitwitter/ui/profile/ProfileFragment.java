package com.example.minitwitter.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.data.ProfileViewModel;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel profileViewModel;
    private TextInputLayout edtUserName, edtPass, edtEmail, edtWebsite, edtDescription;
    private MaterialButton btnSave, btnChanfePass;
    private CircleImageView imgUser;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        initComponents(view);
        setListeners();
        getData();
        return view;
    }

    private void getData() {
        profileViewModel.userProfile.observe(getActivity(), responseUserProfile -> {
            edtUserName.getEditText().setText(responseUserProfile.getUsername());
            edtEmail.getEditText().setText(responseUserProfile.getEmail());
            edtWebsite.getEditText().setText(responseUserProfile.getWebsite());
            edtDescription.getEditText().setText(responseUserProfile.getDescripcion());
            if (!responseUserProfile.getPhotoUrl().isEmpty()){
                Glide.with(getActivity())
                        .load(Constantes.API_MINITWITTER_FILES_URL + responseUserProfile.getPhotoUrl())
                        .into(imgUser);
            } else {
                Glide.with(getActivity())
                        .load(R.drawable.ic_account_circle)
                        .into(imgUser);
            }
        });
    }

    private void initComponents(View view) {
        edtUserName = view.findViewById(R.id.edtUserName);
        edtEmail = view.findViewById(R.id.edtUserEmail);
        edtPass = view.findViewById(R.id.edtUserPass);
        edtWebsite = view.findViewById(R.id.edtUserWebsite);
        edtDescription = view.findViewById(R.id.edtUserDescription);
        btnChanfePass = view.findViewById(R.id.btnChangePass);
        btnSave = view.findViewById(R.id.btnSave);
        imgUser = view.findViewById(R.id.imgProfileUser);
    }

    private void setListeners() {
        btnSave.setOnClickListener(this);
        btnChanfePass.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                Toast.makeText(getActivity(), "Click on save", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnChangePass:
                Toast.makeText(getActivity(), "Click on change pass", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
