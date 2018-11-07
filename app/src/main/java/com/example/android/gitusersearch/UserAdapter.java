package com.example.android.gitusersearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(@NonNull Context context, List<User> user) {
        super(context, 0, user);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);

        User currentUser = getItem(position);

        TextView usernameTextView = listItemView.findViewById(R.id.username_textview);
        usernameTextView.setText(currentUser.getUserName());

        return listItemView;
    }
}
