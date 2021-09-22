package com.postpc.Sheed.Connections.YourFriends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.postpc.Sheed.Chat.ChatActivity;
import com.postpc.Sheed.Chat.ChatFragment;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.database.SheedUsersDB;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourFriendsAdapter extends RecyclerView.Adapter<YourFriendsAdapter.viewHolder> {

    Context context;
    SheedUsersDB db;
    List<String> friends;

    public YourFriendsAdapter(Context context, List<String> friends) {
        this.context = context;
        this.friends = friends;
        db = SheedApp.getDB();
    }

    @NonNull
    @Override
    public com.postpc.Sheed.Connections.YourFriends.YourFriendsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.your_friends_item, parent, false);
        return new com.postpc.Sheed.Connections.YourFriends.YourFriendsAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.postpc.Sheed.Connections.YourFriends.YourFriendsAdapter.viewHolder holder, int position) {
        String userId = friends.get(position);

        db.downloadUserAndDo(userId, sheedUser -> {
            if(sheedUser.equals(null)){
                return;
            }
            holder.userName.setText(sheedUser.firstName);
            Picasso.with(context).load(sheedUser.imageUrl).into(holder.userImage);
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.friends.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        CircleImageView userImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userImage = itemView.findViewById(R.id.userImage);
        }
    }
}
