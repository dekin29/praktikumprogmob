package com.example.praktikumprogmob;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.praktikumprogmob.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    EditText etName;
    EditText etEmail;
    EditText etNoTelp;
    Button btnUpdate;
    private ImageView ivUser;
    private ImageView ivEdit;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        ivUser = (ImageView) findViewById(R.id.img_user);
        ImageView ivEdit = (ImageView) findViewById(R.id.edit_img);

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
            }
        });

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
            }
        });



        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setTitle("Edit Profile");
        mContext = this;
        loading = ProgressDialog.show(mContext, null, "Loading...", true, false);
        initComponents();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initComponents() {
        etName = (EditText) findViewById(R.id.et_nama_field);
        etEmail = (EditText) findViewById(R.id.et_email_field);
        etNoTelp = (EditText) findViewById(R.id.et_notelp);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        profile = getSharedPreferences("profile", Context.MODE_PRIVATE);
        String token = "Bearer "+profile.getString("access_token",null);
        mApiService = UtilsApi.getAPIServiceWithToken(token);
        mApiService.detailUser()
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            String nama = response.body().getName();
                            String email = response.body().getEmail();
                            String notelp = response.body().getNotelp();
                            image = response.body().getProfileimage();


                            etName.setText(nama);
                            etEmail.setText(email);
                            etNoTelp.setText(notelp);

                            Glide.with(EditProfile.this)
                                    .load(UtilsApi.BASE_URL_API+"/profileimages/"+image)
                                    .into(ivUser);

                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean anyError = false;
                if (etName.getText().toString().equals("")){
                    etName.setError("Field tidak boleh kosong");
                    anyError = true;
                }

                if (etEmail.getText().toString().equals("")){
                    etEmail.setError("Field tidak boleh kosong");
                    anyError = true;
                }

                if (etNoTelp.getText().toString().equals("")){
                    etNoTelp.setError("Field tidak boleh kosong");
                    anyError = true;
                }

                if(!anyError){
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    requestUpdate();
                }

            }
        });

    }

    private void requestUpdate(){
        Integer id = profile.getInt("id",0);
        RequestBody formid = RequestBody.create(MediaType.parse("text/plain"), id+"");
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), etName.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), etEmail.getText().toString());
        RequestBody notelp = RequestBody.create(MediaType.parse("text/plain"), etNoTelp.getText().toString());
        Log.d("debug",fileToUpload+" nama : "+filename);
        mApiService.editProfile(formid, name, email, notelp, fileToUpload, filename)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    Toast.makeText(mContext, "Perubahan berhasil disimpan", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(uri != null){
            String filePath = getRealPathFromURIPath(uri, EditProfile.this);
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, EditProfile.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK){
            uri = data.getData();
            ivUser.setImageURI(uri);
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, EditProfile.this);
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

