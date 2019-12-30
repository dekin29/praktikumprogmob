package com.example.praktikumprogmob.APIHelper;

public class UtilsApi {
    // 10.0.2.2 ini adalah localhost.
    public static final String BASE_URL_API = "https://1ce05940.ngrok.io/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }

    public static BaseApiService getAPIServiceWithToken(String token){
        return RetrofitClient.getClientWithToken(BASE_URL_API,token).create(BaseApiService.class);
    }

}
