package com.example.android.gitusersearch;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static GithubApi githubApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        githubApi = retrofit.create(GithubApi.class);
    }

    public static GithubApi getApi() {
        return githubApi;
    }
}
