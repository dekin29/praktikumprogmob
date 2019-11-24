package com.example.praktikumprogmob.APIHelper;

public class UtilsApi {
    // 10.0.2.2 ini adalah localhost.
    public static final String BASE_URL_API = "https://progmob8.herokuapp.com/api/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
