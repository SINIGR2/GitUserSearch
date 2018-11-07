package com.example.android.gitusersearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<User>> {

    private static final String SEARCH_REQUEST_URL = "https://api.github.com/search/users";
    private UserAdapter mAdapter;
    private static final int USER_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameEditText = findViewById(R.id.search_edittext);
                username = usernameEditText.getText().toString();

                final ListView usersListView = findViewById(R.id.user_list);

                mEmptyStateTextView = findViewById(R.id.empty_view);
                usersListView.setEmptyView(mEmptyStateTextView);

                LoaderManager loaderManager;

                Toast msg;

                if (username.isEmpty()) {
                    msg = Toast.makeText(getApplicationContext(), "Введено пустое имя!", Toast.LENGTH_SHORT);
                    msg.show();
                    return;
                } else {
                    mAdapter = new UserAdapter(MainActivity.this, new ArrayList<User>());
                    usersListView.setAdapter(mAdapter);

                    usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            User currentUser = mAdapter.getItem(position);

                            final ImageView avatarImageView = findViewById(R.id.avatar_imageview);

                            avatarImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    usersListView.setVisibility(View.VISIBLE);
                                    avatarImageView.setVisibility(View.GONE);
                                }
                            });

                            Glide.with(MainActivity.this).load(currentUser.getUserAvatarUrl()).into(avatarImageView);
                            usersListView.setVisibility(View.GONE);
                            avatarImageView.setVisibility(View.VISIBLE);
                        }
                    });

                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        loaderManager = getLoaderManager();
                        if (loaderManager == null)
                            loaderManager.initLoader(USER_LOADER_ID, null, MainActivity.this);
                        else
                            loaderManager.restartLoader(USER_LOADER_ID, null, MainActivity.this);
                    } else {
                        View loadingIndicator = findViewById(R.id.loading_indicator);
                        loadingIndicator.setVisibility(View.GONE);

                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                    }
                }
            }
        });
    }

    public Loader<List<User>> onCreateLoader(int id, Bundle bundle) {

        Uri baseUri = Uri.parse(SEARCH_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", username);

        return new UserLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> users) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_user);

        mAdapter.clear();

        if (users != null && !users.isEmpty()) {
            mAdapter.addAll(users);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {
        mAdapter.clear();
    }
}
