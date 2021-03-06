package com.postpc.Sheed.Connections.YourMatches;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.Sheed.Chat.ChatActivity;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.database.SheedUsersDB;
import com.postpc.Sheed.makeMatches.MatchDescriptor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourMatchesAdapter extends RecyclerView.Adapter<YourMatchesAdapter.viewHolder>{

    Context context;
    SheedUsersDB db;
    List<String> matchesDescriptors;

    public YourMatchesAdapter(Context c, HashMap<String, String> matchesDescriptors) {
        context = c;
        this.matchesDescriptors = new ArrayList<>(matchesDescriptors.values());
        db = SheedApp.getDB();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.your_matches_item, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MatchDescriptor matchDescriptor = MatchDescriptor.fromString(matchesDescriptors.get(position));
        String userId = matchDescriptor.getMatchedWith(db.currentSheedUser.email);
        String matchers = matchDescriptor.getMatchersAsString();
        int matchersNum = matchDescriptor.getMatchersNumber();
        int sheedUserFriendsNum = db.currentSheedUser.community.size();

        db.downloadUserAndDo(userId, sheedUser -> {
            if(sheedUser.equals(null)){
                return;
            }
            holder.userName.setText(sheedUser.firstName);
            holder.userAge.setText(sheedUser.age.toString() + " years old");
            holder.matchers.setText(matchers);
            holder.ratingBar.setRating((float) matchersNum / (float) sheedUserFriendsNum);
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
        return matchesDescriptors.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userAge;
        TextView matchers;
        RatingBar ratingBar;
        CircleImageView userImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userAge = itemView.findViewById(R.id.userAge);
            matchers = itemView.findViewById(R.id.matchers);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            userImage = itemView.findViewById(R.id.userImage);
        }
    }
}
