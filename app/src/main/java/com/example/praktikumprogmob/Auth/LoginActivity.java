package com.example.praktikumprogmob.Auth;

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
import com.example.praktikumprogmob.Main2Activity;
import com.example.praktikumprogmob.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    TextView tvRegister;
    ProgressDialog loading;

    private SharedPreferences profile;
    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        initComponents();
        profile = getSharedPreferences("profile", Context.MODE_PRIVATE);
        final Intent toMain = new Intent(LoginActivity.this, Main2Activity.class);
        if(profile.contains("id")){
            startActivity(toMain);
            finish();
        }
    }

    private void initComponents() {
        etEmail = (EditText) findViewById(R.id.et_email_field);
        etPassword = (EditText) findViewById(R.id.et_password_field);
        btnLogin = (Button) findViewById(R.id.btn_sign_in);
        tvRegister = (TextView) findViewById(R.id.tv_not_have_account);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean anyError = false;
                if (etEmail.getText().toString().equals("")){
                    etEmail.setError("Email tidak boleh kosong");
                    anyError = true;
                }

                if (etPassword.getText().toString().equals("")){
                    etPassword.setError("Password tidak boleh kosong");
                    anyError = true;
                }

                if(!anyError){
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    requestLogin();
                }

            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegisterActivity.class));
                finish();
            }
        });
    }

    private void requestLogin(){
        mApiService.loginRequest(etEmail.getText().toString(), etPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    // Jika login berhasil maka data nama yang ada di response API
                                    // akan diparsing ke activity selanjutnya.
                                    Toast.makeText(mContext, "Berhasil Login", Toast.LENGTH_SHORT).show();
                                    int id = jsonRESULTS.getJSONObject("user").getInt("id");
                                    String token = jsonRESULTS.getJSONObject("user").getString("token");
                                    Log.d("debug","id : "+id+" Token :"+token);

                                    //Save to SharedPreferences
                                    SharedPreferences profile = getSharedPreferences( "profile", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor profileEditor = profile.edit();
                                    profileEditor.putInt("id",id);
                                    profileEditor.putString("access_token" , token);
                                    profileEditor.apply();

                                    Intent intent = new Intent(mContext, Main2Activity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Jika login gagal
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
