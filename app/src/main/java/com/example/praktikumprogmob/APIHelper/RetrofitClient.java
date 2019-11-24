package com.example.praktikumprogmob.APIHelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.praktikumprogmob.Main2Activity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        if (retrofit == null) {


            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

//    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            SharedPreferences  sharedPreferences = mContext.getSharedPreferences("profile",
//                    Context.MODE_PRIVATE);
//            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiYWVkNWI2NzNhMzU2M2Q1NGEzYTVmY2I0NTk4ODlhM2RlNWUxNjk2MDdkODE5NGZkYTQ0MjdmNmE0YmEyNGNjNzliZTdmNGU2ZTliZTQwMjciLCJpYXQiOjE1NzQwNTA4OTksIm5iZiI6MTU3NDA1MDg5OSwiZXhwIjoxNjA1NjczMjk5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.q-Y1B5j7wvxVMZJr-K6odsfEYUTePkddsQXwEP4RWtoeWf5kYFhzf___Deqn2l1Xn7xmlFaZIxpSkGFtWqPJwV7OPB-TbnXG7bfqgUVPcvArJfErF0uaGK552koUhVa5zPoid-shX5FZyUhmx7mbgXhMs0kztS2fV8ioW26eAaHJn6vtIvFsnXF6wR0GdNT4O_cxZuPZO2Lcal2Qqtp4c8SmDwIYeq7aJ_gIKcGv0cs2NbYIamBveeuEPDR5zl1VBZjdJ2EuipOxpU6lpiW2bul2mCKBfTjs7ESAOS5K77rWAlwfQxgvxZfs5tt2B5qgB5kT3VXglSCneJtKNin_QY3XYxTx9gEvfbY57anEI0DbP5sD3lq-epiAVtJKB2oQ6Kw5SNC0PfaMSNp8pWmomhQ1MIpuZCJYBc1Mj3YnvSznTPmj2LXAUEf2G3HQike_PMH5PaApVAjXtxDZM2o8wgVJXB13MQne_O1qMEIJ6m0jSqzGNqkWcFyHiI-FRBbBLueUgoNl-aMkprnjuwPolM6fr0kJxH4gPShz77oj2Apu8TVZ2GVFfn5tpiHukk6Jm-JBrfYO39QOXQqpt-DfJmzcazAhewdLWII3TgXylIo1e9SgM5QSlAmtq0_t_ySvmB-FNrWh43qZy8sAvB2gvGKv49uwY3pCf2vp7OAodmg";
//            Request newRequest  = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer " + token)
//                    .build();
//            return chain.proceed(newRequest);
//        }
//    }).build();

}
