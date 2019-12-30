package com.example.praktikumprogmob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.praktikumprogmob.APIHelper.BaseApiService;
import com.example.praktikumprogmob.APIHelper.UtilsApi;
import com.example.praktikumprogmob.Adapter.KomentarAdapter;
import com.example.praktikumprogmob.Adapter.PostAdapter;
import com.example.praktikumprogmob.Database.AppDatabase;
import com.example.praktikumprogmob.Database.AppExecutors;
import com.example.praktikumprogmob.Model.DetailPostResult;
import com.example.praktikumprogmob.Model.Komentar;
import com.example.praktikumprogmob.Model.Post;
import com.example.praktikumprogmob.Model.PostResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPost extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SharedPreferences profile;
    Context mContext;
    private AppDatabase mDb;
    ProgressDialog loading;
    BaseApiService mApiService;
    private KomentarAdapter adapter;
    private ArrayList<Post> postArrayList;
    private ArrayList<Komentar> komenArrayList;
    TextView tvJudul, tvDeskripsi, tvKontak, tvKategori,jmlKomen;
    EditText etKomen;
    Button btnKomen, btnClaim, btnUpdate, btnDelete, btnFound;
    ImageView ivFoto;
    View view;
    Post data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_post);
        mContext = this;
        mDb = AppDatabase.getInstance(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        final int idPost = extras.getInt("idPost");
        Log.d("debug","Judul Post : "+idPost);
        loading = ProgressDialog.show(mContext, null, "Tunggu ya :)", true, false);
        initComponents();
        loadPost(idPost);
        btnKomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                newKomen(idPost);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                deletePost(idPost);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putInt("idPost",idPost);
                Intent intent = new Intent(mContext, UpdatePost.class);
                intent.putExtras(extras);
                mContext.startActivity(intent);
                finish();
            }
        });

        btnFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                foundIt(idPost);
            }
        });

        btnClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                claimed(idPost);
            }
        });

    }

    private void loadPost(final int idPost) {
        AppExecutors.getInstance().diskIO().execute(new Runnable(){
            @Override
            public void run() {
                profile = getSharedPreferences("profile", Context.MODE_PRIVATE);
                String token = "Bearer " + profile.getString("access_token", null);
                mApiService = UtilsApi.getAPIServiceWithToken(token);
                Log.d("debug","ID Post : "+idPost);
                Call<DetailPostResult> posts = mApiService.detailPost(idPost);
                posts.enqueue(new Callback<DetailPostResult>() {
                    @Override
                    public void onResponse(Call<DetailPostResult> call, Response<DetailPostResult> response) {

                        Log.d("debug","Judul Post : "+response.body().getPosts().get(0).getJudul());

                        if(mContext != null){
                            Glide.with(mContext)
                                    .load(UtilsApi.BASE_URL_API + response.body().getPosts().get(0).getFoto())
                                    .into(ivFoto);
                        }
                        tvJudul.setText(response.body().getPosts().get(0).getJudul());
                        tvDeskripsi.setText(response.body().getPosts().get(0).getDeskripsi());
                        tvKontak.setText(response.body().getPosts().get(0).getKontak());
                        tvKategori.setText(response.body().getPosts().get(0).getNama_kategori());
                        jmlKomen.setText('('+String.valueOf(response.body().getKomentars().size())+')');
                        int id = profile.getInt("id", 0);

//                        data = new Post(
//                                idPost,id,response.body().getPosts().get(0).getId_kategori(),response.body().getPosts().get(0).getJudul(),response.body().getPosts().get(0).getFoto(),response.body().getPosts().get(0).getDeskripsi(),response.body().getPosts().get(0).getKontak(),response.body().getPosts().get(0).getCreated_at()
//                        );

                        if (id == response.body().getPosts().get(0).getId_user()){
                            btnDelete.setVisibility(View.VISIBLE);
                            btnUpdate.setVisibility(View.VISIBLE);
                        }else if(response.body().getPosts().get(0).getId_kategori() == 1){
                            btnFound.setVisibility(View.VISIBLE);
                        }else if(response.body().getPosts().get(0).getId_kategori() == 2){
                            btnClaim.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(mContext, "Terjadi Kesalahan!", Toast.LENGTH_SHORT).show();
                        }


                        komenArrayList = response.body().getKomentars();
                        Log.d("debug", "komen : " + komenArrayList);
                        adapter = new KomentarAdapter(mContext, komenArrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<DetailPostResult> call, Throwable t) {
                        loading.dismiss();
                    }
                });
            }
        });
    }

    private void newKomen(final int idPost){
        RequestBody id_post = RequestBody.create(MediaType.parse("text/plain"), idPost+"");
        RequestBody komentar = RequestBody.create(MediaType.parse("text/plain"), etKomen.getText().toString());
        mApiService.newKomentar(id_post, komentar)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL KOMEN");
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("pesan").equals("berhasil")){
                                    Toast.makeText(mContext, "Sukses Berkomentar", Toast.LENGTH_SHORT).show();
                                    etKomen.setText("");
                                    adapter.notifyDataSetChanged();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: GA BERHASIL KOMEN");
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deletePost(final int idPost){
        mApiService.deletePost(idPost)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("pesan").equals("berhasil")){
                                    mDb.postDao().deletePost(data);
                                    Toast.makeText(mContext, "Post Dihapus!", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    private void foundIt(final int idPost){
        mApiService.foundIt(idPost)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            Toast.makeText(mContext, "Berhasil!", Toast.LENGTH_SHORT).show();
                        } else {
                            loading.dismiss();
                            Toast.makeText(mContext, "Gagal!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    private void claimed(final int idPost){
        int id_user = profile.getInt("id",0);
        mApiService.claimed(idPost,id_user)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            Toast.makeText(mContext, "Berhasil!", Toast.LENGTH_SHORT).show();
                        } else {
                            loading.dismiss();
                            Toast.makeText(mContext, "Gagal!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    private void initComponents(){
        tvJudul = findViewById(R.id.detail_judul);
        tvKontak = findViewById(R.id.kontak);
        tvDeskripsi = findViewById(R.id.deskripsi);
        tvKategori = findViewById(R.id.kategori);
        ivFoto = findViewById(R.id.postImage);
        recyclerView = findViewById(R.id.rv_komen);
        jmlKomen = findViewById(R.id.jml_komentar);
        etKomen = findViewById(R.id.kolomkomen);
        btnKomen = findViewById(R.id.btnkomen);
        btnClaim = findViewById(R.id.btn_claim);
        btnFound = findViewById(R.id.btn_found);
        btnUpdate = findViewById(R.id.btn_update_post);
        btnDelete = findViewById(R.id.btn_delete_post);

    }

}

