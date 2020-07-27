package com.project.maqdoom.data.remote.api_rest;



import com.project.maqdoom.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String Base_Url = BuildConfig.BASE_SERVER_URL+"/";

    public static Retrofit getClient() {
        Retrofit retrofit;
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        retrofit = new Retrofit.Builder()
                .baseUrl(Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
        return retrofit;
    }

}
