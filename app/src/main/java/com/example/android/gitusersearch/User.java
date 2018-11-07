package com.example.android.gitusersearch;

public class User {

    private String mUserName;
    private String mUserAvatarUrl;

    public User(String UserName, String UserAvatarUrl){
        mUserName = UserName;
        mUserAvatarUrl = UserAvatarUrl;
    }

    public String getUserName(){
        return mUserName;
    }

    public String getUserAvatarUrl(){
        return mUserAvatarUrl;
    }
}
