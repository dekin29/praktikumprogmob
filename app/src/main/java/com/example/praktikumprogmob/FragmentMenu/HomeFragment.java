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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikumprogmob.APIHelper.BaseApiService;
import com.example.praktikumprogmob.APIHelper.UtilsApi;
import com.example.praktikumprogmob.Adapter.PostAdapter;
import com.example.praktikumprogmob.BuatPost;
import com.example.praktikumprogmob.EditProfile;
import com.example.praktikumprogmob.Model.Post;
import com.example.praktikumprogmob.Model.PostResult;
import com.example.praktikumprogmob.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SharedPreferences profile;
    ProgressDialog loading;
    BaseApiService mApiService;
    private PostAdapter adapter;
    private ArrayList<Post> postArrayList;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BuatPost.class);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_post);
        loading = ProgressDialog.show(getActivity(), null, "Tunggu ya :)", true, false);
        loadPost();
    }

    private void loadPost() {
        profile = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
        String token = "Bearer "+profile.getString("access_token",null);
        mApiService = UtilsApi.getAPIServiceWithToken(token);
        Call<PostResult> posts = mApiService.allPost();
        posts.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                postArrayList = response.body().getPosts();
                Log.d("debug","isi : "+postArrayList);
                adapter = new PostAdapter(getActivity(),postArrayList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                loading.dismiss();
            }
        });
    }

}
