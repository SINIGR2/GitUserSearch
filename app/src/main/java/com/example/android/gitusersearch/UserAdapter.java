package com.example.android.gitusersearch;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User.Item> {//RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User.Item> users;
    private Context mContext;
    private ImageView avatar;
    private TextView name;

    private List<ImageView> avatars;

    public UserAdapter(@NonNull Context context, List<User.Item> users, List<ImageView> avatars) {
        super(context, 0, users);
        mContext = context;
        this.users = users;
        this.avatars = avatars;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        listItemView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);

        avatar = listItemView.findViewById(R.id.avatar_image_view);
        name = listItemView.findViewById(R.id.username_text_view);

        Glide.with(mContext).load(users.get(position).getAvatarUrl()).into(avatar);

        if (avatars.size() <= position)
            //if (avatars.get(position) != avatar)
                avatars.add(position, avatar);

        name.setText(users.get(position).getLogin());

        return listItemView;
    }
}
