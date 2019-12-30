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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.praktikumprogmob.APIHelper.BaseApiService;
import com.example.praktikumprogmob.APIHelper.UtilsApi;
import com.example.praktikumprogmob.Adapter.KomentarAdapter;
import com.example.praktikumprogmob.Database.AppDatabase;
import com.example.praktikumprogmob.Database.AppExecutors;
import com.example.praktikumprogmob.Model.DetailPostResult;
import com.example.praktikumprogmob.Model.Post;
import com.example.praktikumprogmob.Model.User;

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

public class UpdatePost extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    EditText etJudul, etDeskripsi, etKontak, etKategori;
    ImageView ivFoto;
    Integer id_kategori;
    String kategori;
    Button btnUpdate;
    private String image;
    MultipartBody.Part fileToUpload;
    RequestBody filename;

    ProgressDialog loading;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;

    private SharedPreferences profile;
    Context mContext;
    BaseApiService mApiService;
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_post);
        mContext = this;
        mDb = AppDatabase.getInstance(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        final int idPost = extras.getInt("idPost");
        initComponents(idPost);
        loading = ProgressDialog.show(mContext, null, "Loading...", true, false);
        loadPost(idPost);

        ivFoto.setOnClickListener(new View.OnClickListener() {
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePost.this);
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


    }

    private void initComponents(final int idPost) {
        ivFoto = findViewById(R.id.img_editpost);
        etJudul = findViewById(R.id.et_editjudul);
        etDeskripsi = findViewById(R.id.et_editdeskripsi);
        etKategori = findViewById(R.id.et_editkategori);
        etKontak = findViewById(R.id.et_editkontak);
        btnUpdate = findViewById(R.id.btn_editpost);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean anyError = false;
                if (etJudul.getText().toString().equals("")){
                    etJudul.setError("Field tidak boleh kosong");
                    anyError = true;
                }

                if (etDeskripsi.getText().toString().equals("")){
                    etDeskripsi.setError("Field tidak boleh kosong");
                    anyError = true;
                }

                if (etKategori.getText().toString().equals("")){
                    etKategori.setError("Field tidak boleh kosong");
                    anyError = true;
                }

                if (etKontak.getText().toString().equals("")){
                    etKontak.setError("Field tidak boleh kosong");
                    anyError = true;
                }

                if(!anyError){
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    requestUpdate(idPost);
                }

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
                        etJudul.setText(response.body().getPosts().get(0).getJudul());
                        etDeskripsi.setText(response.body().getPosts().get(0).getDeskripsi());
                        etKontak.setText(response.body().getPosts().get(0).getKontak());
                        etKategori.setText(response.body().getPosts().get(0).getNama_kategori());
                        id_kategori = response.body().getPosts().get(0).getId_kategori();
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

    private void requestUpdate(final int idPost){
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), idPost+"");
        RequestBody kategori = RequestBody.create(MediaType.parse("text/plain"), id_kategori+"");
        RequestBody judul = RequestBody.create(MediaType.parse("text/plain"), etJudul.getText().toString());
        RequestBody  deskripsi= RequestBody.create(MediaType.parse("text/plain"), etDeskripsi.getText().toString());
        RequestBody kontak = RequestBody.create(MediaType.parse("text/plain"), etKontak.getText().toString());
        Log.d("debug",fileToUpload+" nama : "+filename);
        mApiService.updatePost(id, judul, deskripsi, kontak, kategori, fileToUpload, filename)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("pesan").equals("berhasil")){
                                    Date datenow = Calendar.getInstance().getTime();
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    String strDate = dateFormat.format(datenow);
                                    int id_user = profile.getInt("id",0);
                                    final Post data = new Post(
                                            idPost,id_user,id_kategori,etJudul.getText().toString(),"/postimage/prog.jpg",etDeskripsi.getText().toString(),etKontak.getText().toString(),strDate
                                    );
                                    AppExecutors.getInstance().diskIO().execute(new Runnable(){
                                        @Override
                                        public void run() {
                                            mDb.postDao().updatePost(data);
                                            ((UpdatePost)mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(mContext, "Perubahan berhasil disimpan", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                        }
                                    });
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(uri != null){
            String filePath = getRealPathFromURIPath(uri, UpdatePost.this);
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, UpdatePost.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK){
            uri = data.getData();
            ivFoto.setImageURI(uri);
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, UpdatePost.this);
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
