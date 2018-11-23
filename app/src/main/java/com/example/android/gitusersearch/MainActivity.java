package com.example.android.gitusersearch;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

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

    private List<ImageView> avatars;
    private List<User.Item> users;
    private ProgressDialog pDialog;

    private ListView userListView;
    private ImageButton searchButton;
    private ImageView avatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        users = new ArrayList<>();
        avatars = new ArrayList<>();
        userListView = findViewById(R.id.user_list);
        searchButton = findViewById(R.id.search_button);
        avatarImageView = findViewById(R.id.avatar_image_view);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameEditText = findViewById(R.id.search_edittext);
                username = usernameEditText.getText().toString().trim();

                if (username.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Не задано имя для поиска", Toast.LENGTH_SHORT);
                    return;
                }

                users = new ArrayList<>();
                avatars = new ArrayList<>();

                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.show();

                App.getApi().getUser(username).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            hidePDialog();
                            users.addAll(response.body().getItems());
                            mAdapter = new UserAdapter(MainActivity.this, users, avatars);
                            userListView.setAdapter(mAdapter);
                        } else {
                            System.out.println(response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        hidePDialog();
                    }
                });
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                avatarImageView.setImageDrawable(avatars.get(position).getDrawable());
                avatarImageView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.avatar_open_animation));
                avatarImageView.setVisibility(View.VISIBLE);

            }
        });

        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarImageView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.avatar_close_animation));
                avatarImageView.setVisibility(View.GONE);
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
