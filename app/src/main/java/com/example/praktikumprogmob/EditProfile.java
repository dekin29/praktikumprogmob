package com.example.praktikumprogmob;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.praktikumprogmob.APIHelper.BaseApiService;
import com.example.praktikumprogmob.APIHelper.UtilsApi;
import com.example.praktikumprogmob.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    EditText etName;
    EditText etEmail;
    EditText etNoTelp;
    Button btnUpdate;
    ProgressDialog loading;

    private SharedPreferences profile;
    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

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
        mApiService = UtilsApi.getAPIService();
        String token = "Bearer "+profile.getString("access_token",null);
        mApiService.getUser(token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            String nama = response.body().getName();
                            String email = response.body().getEmail();
                            String notelp = response.body().getNotelp();

                            etName.setText(nama);
                            etEmail.setText(email);
                            etNoTelp.setText(notelp);

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
        mApiService.editProfile(id, etName.getText().toString(), etEmail.getText().toString(), etNoTelp.getText().toString())
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

}

