package com.example.praktikumprogmob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.praktikumprogmob.APIHelper.BaseApiService;
import com.example.praktikumprogmob.APIHelper.UtilsApi;
import com.example.praktikumprogmob.Adapter.Post2Adapter;
import com.example.praktikumprogmob.Auth.LoginActivity;
import com.example.praktikumprogmob.Database.AppDatabase;
import com.example.praktikumprogmob.Database.AppExecutors;
import com.example.praktikumprogmob.Model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuatPost extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;

    private SharedPreferences profile;
    Context mContext;
    BaseApiService mApiService;
    MultipartBody.Part fileToUpload;
    RequestBody filename;
    ImageView ivPost;
    EditText etJudul;
    EditText etDeskripsi;
    EditText etKontak;
    EditText etKategori;
    EditText etLokasi;
    Button btnPost;
    String token;
    Integer id;
    Integer id_kategori;
    ProgressDialog loading;
    String kategori;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buat_post);
        mContext = this;
        mDb = AppDatabase.getInstance(getApplicationContext());

        initComponents();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setTitle("Buat Post");

        ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
            }
        });

        etKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(BuatPost.this);
                builder.setTitle("Pilih Kategori");
                // add a radio button list
                final String[] cat = {"Kehilangan", "Penemuan"};
                int checkedItem = 1; // cow
                builder.setSingleChoiceItems(cat, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        id_kategori = which+1;
                        kategori = cat[which];
                        Log.d("kategoti",""+kategori);
                    }
                });
                // add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etKategori.setText(kategori);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                onSubmit();
            }
        });
    }

    private void initComponents(){
        profile = getSharedPreferences("profile", Context.MODE_PRIVATE);
        token = "Bearer "+profile.getString("access_token",null);
        id = profile.getInt("id",0);

        ivPost = findViewById(R.id.img_post);
        etJudul = findViewById(R.id.et_judul);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etKontak = findViewById(R.id.et_kontak);
        btnPost = findViewById(R.id.btn_post);
        etKategori = findViewById(R.id.et_kategori);
//        etLokasi = findViewById(R.id.et_lokasi);
    }

    private void onSubmit(){
        RequestBody kategori = RequestBody.create(MediaType.parse("text/plain"), id_kategori+"");
        RequestBody judul = RequestBody.create(MediaType.parse("text/plain"), etJudul.getText().toString());
        RequestBody  deskripsi= RequestBody.create(MediaType.parse("text/plain"), etDeskripsi.getText().toString());
        RequestBody kontak = RequestBody.create(MediaType.parse("text/plain"), etKontak.getText().toString());
        Log.d("debug",fileToUpload+" nama : "+filename + kategori + judul +deskripsi + kontak);
        profile = getSharedPreferences("profile", Context.MODE_PRIVATE);
        String token = "Bearer "+profile.getString("access_token",null);
        mApiService = UtilsApi.getAPIServiceWithToken(token);
        mApiService.newPost(judul, deskripsi, kontak, kategori, fileToUpload, filename)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("pesan").equals("berhasil")){
                                    Date datenow = Calendar.getInstance().getTime();
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    String strDate = dateFormat.format(datenow);
                                    int id_user = profile.getInt("id",0);
                                    final Post data = new Post(
                                            id_user, id_kategori,etJudul.getText().toString(),"/postimage/prog.jpg",etDeskripsi.getText().toString(), etKontak.getText().toString(), strDate
                                    );
                                    AppExecutors.getInstance().diskIO().execute(new Runnable(){
                                        @Override
                                        public void run() {
                                            mDb.postDao().insertPost(data);
                                            ((BuatPost)mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    loading.dismiss();
                                                    Toast.makeText(mContext, "Post berhasil dibuat", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    loading.dismiss();
                                    Toast.makeText(mContext, "Gagal Post", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(uri != null){
            String filePath = getRealPathFromURIPath(uri, BuatPost.this);
            File file = new File(filePath);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), mFile);
            filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            Log.d("debug","file : "+fileToUpload+"name : "+filename);

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d("debug", "Permission has been denied");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, BuatPost.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK){
            uri = data.getData();
            ivPost.setImageURI(uri);
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, BuatPost.this);
                File file = new File(filePath);
                Log.d("debug", "Filename " + file.getName());
                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), mFile);
                filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                Log.d("debug","file : "+fileToUpload+"name : "+filename);
            }else{
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}
