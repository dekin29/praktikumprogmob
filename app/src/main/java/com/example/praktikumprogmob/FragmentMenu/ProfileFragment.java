package com.example.praktikumprogmob.FragmentMenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.praktikumprogmob.APIHelper.BaseApiService;
import com.example.praktikumprogmob.APIHelper.RetrofitClient;
import com.example.praktikumprogmob.APIHelper.UtilsApi;
import com.example.praktikumprogmob.Auth.LoginActivity;
import com.example.praktikumprogmob.EditProfile;
import com.example.praktikumprogmob.GantiPassword;
import com.example.praktikumprogmob.Model.User;
import com.example.praktikumprogmob.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment {
    View view;
    ImageView ivUser;
    TextView tvNama;
    TextView tvEmail;
    Button btnEdit;
    Button btnPassword;
    Button btnLogout;
    private SharedPreferences profile;
    BaseApiService mApiService;
    ProgressDialog loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        ivUser = view.findViewById(R.id.img_user);
        tvNama = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        btnEdit = view.findViewById(R.id.btn_edit_profile);
        btnPassword = view.findViewById(R.id.btn_ganti_password);
        btnLogout = view.findViewById(R.id.btn_logout);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
                SharedPreferences.Editor profileEditor = profile.edit();
                profileEditor.clear();
                profileEditor.commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loading = ProgressDialog.show(getActivity(), null, "Tunggu ya :)", true, false);

        profile = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
        String token = "Bearer "+profile.getString("access_token",null);

        mApiService = UtilsApi.getAPIServiceWithToken(token);
        mApiService.detailUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String nama = response.body().getName();
                String email = response.body().getEmail();
                String image = response.body().getProfileimage();

                tvNama.setText(nama);
                tvEmail.setText(email);

                Glide.with(getActivity())
                        .load(UtilsApi.BASE_URL_API + "/profileimages/" + image)
                        .into(ivUser);

                loading.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("debug","GAGAL");
            }
        });
    }

}
