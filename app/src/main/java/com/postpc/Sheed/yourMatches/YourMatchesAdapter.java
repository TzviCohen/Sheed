package com.postpc.Sheed.yourMatches;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.database.SheedUsersDB;

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
            holder.matchMadeBy.setText("Made by\n" + matchMadeBy);
//            Picasso.with(context).load(sheedUser.imageUrl).noFade().into(holder.userImage);
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
//            Picasso.with(this).load(itemView.imageUrl).into(profileImage);


            // Picasso.with(this).load(sheedUser.imageUrl).into(lhsImage);
            chatButton = itemView.findViewById(R.id.chatButton);
        }
    }
}