package com.example.android.gitusersearch;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity
        extends AppCompatActivity {

    private static final String SEARCH_REQUEST_URL = "https://api.github.com/";
    private UserAdapter mAdapter;
    private static final int USER_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private String username;

    private List<User.Item> users;
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        users = new ArrayList<>();
        recyclerView = findViewById(R.id.user_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ImageButton searchButton = findViewById(R.id.search_button);

        mAdapter = new UserAdapter(MainActivity.this, users);
        recyclerView.setAdapter(mAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameEditText = findViewById(R.id.search_edittext);
                username = usernameEditText.getText().toString();

                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.show();

                App.getApi().getUser(username).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            hidePDialog();
                            users.addAll(response.body().getItems());
                            mAdapter.notifyDataSetChanged();
                            recyclerView.getAdapter().notifyDataSetChanged();
                        } else {
                            System.out.println(response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        hidePDialog();
                    }
                });

                //Response response = MainActivity.getApi().loadUsers("bob").execute();

                /*final ListView usersListView = findViewById(R.id.user_list);

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
                                    avatarImageView.setVisibility(View.GONE);
                                }
                            });

                            Glide.with(MainActivity.this).load(currentUser.getUserAvatarUrl()).into(avatarImageView);
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
                }*/
            }
        });
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
