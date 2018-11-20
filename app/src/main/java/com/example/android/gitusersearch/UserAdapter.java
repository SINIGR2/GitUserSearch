package com.example.android.gitusersearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

   /* public UserAdapter(@NonNull Context context, List<User> user) {
        super(context, 0, user);
    }*/

    private List<User.Item> users;

    public UserAdapter(List<User.Item> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User.Item user = users.get(position);
        holder.name.setText(user.getLogin());
    }

    @Override
    public int getItemCount() {
        if (users == null)
            return 0;
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username_textview);
        }
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);

        User currentUser = getItem(position);
        //String login = currentUser.getLogin();

        TextView usernameTextView = listItemView.findViewById(R.id.username_textview);
        //usernameTextView.setText(currentUser.getItems().get(position).getLogin()););


        return listItemView;
    }*/
}
