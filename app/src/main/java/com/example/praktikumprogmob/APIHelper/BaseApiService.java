package com.example.praktikumprogmob.APIHelper;

import com.example.praktikumprogmob.Model.PostResult;
import com.example.praktikumprogmob.Model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BaseApiService {
    // Fungsi ini untuk memanggil API login
    @FormUrlEncoded
    @POST("api/login")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    // Fungsi ini untuk memanggil API register
    @FormUrlEncoded
    @POST("api/register")
    Call<ResponseBody> registerRequest(@Field("name") String nama,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("c_password") String cpassword);

    @POST("api/details")
    Call<User> detailUser();

    @FormUrlEncoded
    @POST("api/gantipassword")
    Call<ResponseBody> gantiPassword(@Field("id") Integer id,
                                     @Field("oldpassword") String old_password,
                                     @Field("password") String password,
                                     @Field("c_password") String cpassword);

    @Multipart
    @POST("api/editprofile")
    Call<ResponseBody> editProfile(@Part("id") RequestBody id,
                                   @Part("name") RequestBody name,
                                   @Part("email") RequestBody email,
                                   @Part("notelp") RequestBody notelp,
                                   @Part MultipartBody.Part image,
                                   @Part("filename") RequestBody filename);

    @GET("api/allpost")
    Call<PostResult> allPost();


}


