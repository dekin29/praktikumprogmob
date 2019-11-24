package com.example.praktikumprogmob;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.praktikumprogmob.APIHelper.BaseApiService;
import com.example.praktikumprogmob.APIHelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GantiPassword extends AppCompatActivity {
    EditText etOld;
    EditText etPassword;
    EditText etRepassword;
    Button btnGanti;
    ProgressDialog loading;

    private SharedPreferences profile;
    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ganti_password);
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        mContext = this;

        etOld = (EditText) findViewById(R.id.et_password_old_field);
        etPassword = (EditText) findViewById(R.id.et_password_field);
        etRepassword = (EditText) findViewById(R.id.et_cpassword_field);
        btnGanti = (Button) findViewById(R.id.btn_ganti_password);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setTitle("Ganti Password");

        btnGanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean anyError = false;
                if (etOld.getText().toString().equals("")){
                    etOld.setError("Field tidak boleh kosong");
                    anyError = true;
                }

                if (etPassword.getText().toString().equals("")){
                    etPassword.setError("Field Baru tidak boleh kosong");
                    anyError = true;
                }

                if (etRepassword.getText().toString().equals("")){
                    etRepassword.setError("Field tidak boleh kosong");
                    anyError = true;
                }

                if(!anyError){
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    gantiPasswordMethod();
                }

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void gantiPasswordMethod(){
        SharedPreferences profile = getSharedPreferences( "profile", Context.MODE_PRIVATE);
        int id = profile.getInt("id",0);
        Log.d("debug","shared : "+id+ etOld.getText().toString()+etPassword.getText().toString()+etRepassword.getText().toString());
        mApiService.gantiPassword(id,etOld.getText().toString(), etPassword.getText().toString(), etRepassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    loading.dismiss();
                                    Toast.makeText(mContext, "Password Berhasil diubah", Toast.LENGTH_SHORT).show();
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
                        } else{
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

