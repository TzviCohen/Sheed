package com.postpc.Sheed.yourMatches;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.postpc.Sheed.Chat.MessageFragment;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.database.SheedUsersDB;
import com.squareup.picasso.Picasso;

import java.util.List;

public class YourMatchesAdapter extends RecyclerView.Adapter<YourMatchesAdapter.viewHolder>{

    Context context;
    SheedUsersDB db;
    List<String> users;
    List<String> matchMadeByArray;

    public YourMatchesAdapter(Context c, List<String> usersId, List<String> matchMadeBy) {
        context = c;
        users = usersId;
        matchMadeByArray = matchMadeBy;
        db = SheedApp.getDB();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.your_matches_single_profile_layout, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        String userId = users.get(position);
        String matchMadeBy = matchMadeByArray.get(position);
        db.downloadUserAndDo(userId, sheedUser -> {
            holder.userName.setText(sheedUser.firstName);
            holder.userAge.setText(sheedUser.age.toString());
            holder.matchMadeBy.setText("Matched by\n" + matchMadeBy);
            Picasso.with(context).load(sheedUser.imageUrl).into(holder.userImage);
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment messageFragment = new MessageFragment();
                messageFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, messageFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userAge;
        TextView matchMadeBy;
        ShapeableImageView userImage;
        Button chatButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userAge = itemView.findViewById(R.id.userAge);
            matchMadeBy = itemView.findViewById(R.id.matchMadeBy);
            userImage = itemView.findViewById(R.id.useImage);
        }
    }
}
