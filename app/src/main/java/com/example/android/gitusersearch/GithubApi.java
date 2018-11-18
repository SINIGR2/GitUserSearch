package com.example.android.gitusersearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubApi {

    @GET("search/users")
    Call<User> getUser(@Query("q") String resourceName);
}
