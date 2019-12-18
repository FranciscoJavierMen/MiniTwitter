package com.example.minitwitter.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.data.ProfileViewModel;
import com.example.minitwitter.retrofit.request.RequestUserProfile;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel profileViewModel;
    private TextInputLayout edtUserName, edtPass, edtEmail, edtWebsite, edtDescription;
    private MaterialButton btnSave, btnChanfePass;
    private CircleImageView imgUser;
    private boolean isLoading = true;
    private PermissionListener permissionListener;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
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
        isLoading = false;
        profileViewModel.userProfile.observe(getActivity(), responseUserProfile -> {
            edtUserName.getEditText().setText(responseUserProfile.getUsername());
            edtEmail.getEditText().setText(responseUserProfile.getEmail());
            edtWebsite.getEditText().setText(responseUserProfile.getWebsite());
            edtDescription.getEditText().setText(responseUserProfile.getDescripcion());
            if (!responseUserProfile.getPhotoUrl().isEmpty()){
                Glide.with(getActivity())
                        .load(Constantes.API_MINITWITTER_FILES_URL + responseUserProfile.getPhotoUrl())
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imgUser);
            } else {
                Glide.with(getActivity())
                        .load(R.drawable.profile)
                        .into(imgUser);
            }
            if (!isLoading){
                btnSave.setEnabled(true);
                Toast.makeText(getActivity(), "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show();
            }
        });

        profileViewModel.photoProfile.observe(getActivity(), photo -> {
            if (!photo.isEmpty()){
                Glide.with(getActivity())
                        .load(Constantes.API_MINITWITTER_FILES_URL + photo)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
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
        imgUser.setOnClickListener(this);
    }

    private void checkPermissions(){
        PermissionListener dialogOnDeniedPermission = DialogOnDeniedPermissionListener.Builder
                .withContext(getActivity())
                .withTitle("Permisios para acceso a la galería")
                .withMessage("Se necesaita dar acceso a la galería para poder seleccionar una fotos")
                .withButtonText("Aceptar")
                .withIcon(R.mipmap.ic_launcher)
                .build();

        permissionListener = new CompositePermissionListener(
                (PermissionListener) getActivity(),
                dialogOnDeniedPermission
        );
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(permissionListener)
                .check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateProfile(){
        String username = edtUserName.getEditText().getText().toString();
        String email = edtEmail.getEditText().getText().toString();
        String description = edtDescription.getEditText().getText().toString();
        String website = edtWebsite.getEditText().getText().toString();
        String password = edtPass.getEditText().getText().toString();

        if (TextUtils.isEmpty(username)){
            edtUserName.setError("El nombre de usuario es requerido");
        } else if (TextUtils.isEmpty(email)){
            edtEmail.setError("El email es requerido");
        } else if (TextUtils.isEmpty(description)){
            edtDescription.setError("La descripción es requerida");
        } else if (TextUtils.isEmpty(password)){
            edtPass.setError("La contraseña es requerida");
        } else {
            RequestUserProfile request = new RequestUserProfile(username, email, description, website, password);
            profileViewModel.updateProfile(request);

            Toast.makeText(getActivity(), "Guardando los datos del perfil... ", Toast.LENGTH_SHORT).show();
            btnSave.setEnabled(false);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                updateProfile();
                break;
            case R.id.btnChangePass:

                break;
            case R.id.imgProfileUser:
                checkPermissions();
                break;
        }
    }
}
