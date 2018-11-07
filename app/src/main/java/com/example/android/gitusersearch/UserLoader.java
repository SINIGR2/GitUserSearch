package com.example.android.gitusersearch;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class UserLoader extends AsyncTaskLoader<List<User>> {

    private String mUrl;

    public UserLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<User> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<User> users = QueryUtils.fetchUserData(mUrl);
        return users;
    }
}
