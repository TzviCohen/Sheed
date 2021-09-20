package com.postpc.Sheed.yourMatches;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.postpc.Sheed.Chat.ChatFragment;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.database.SheedUsersDB;
import com.postpc.Sheed.makeMatches.MatchDescriptor;
import com.squareup.picasso.Picasso;

import java.util.List;

public class YourMatchesAdapter extends RecyclerView.Adapter<YourMatchesAdapter.viewHolder>{

    Context context;
    SheedUsersDB db;
//    List<String> users;
//    List<String> matchMadeByArray;
    List<String> matchesDescriptors;

    public YourMatchesAdapter(Context c, List<String> matchesDescriptors) {
        context = c;
        this.matchesDescriptors = matchesDescriptors;
//        matchMadeByArray = matchMadeBy;
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
//        String userId = users.get(position);
//        String matchMadeBy = matchMadeByArray.get(position);
        MatchDescriptor matchDescriptor = MatchDescriptor.fromString(matchesDescriptors.get(position));
        String userId = matchDescriptor.getMatchedWith(db.currentSheedUser.email);
        String matchMadeBy = matchDescriptor.getMatcherName();

        db.downloadUserAndDo(userId, sheedUser -> {
            if(sheedUser.equals(null)){
                return;
            }
            holder.userName.setText(sheedUser.firstName);
            holder.userAge.setText(sheedUser.age.toString());
            holder.matchMadeBy.setText("Matched by\n" + matchMadeBy);
            Picasso.with(context).load(sheedUser.imageUrl).into(holder.userImage);
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
//                bundle.putString("currentUser", userId);
                bundle.putString("userId", userId);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment messageFragment = new ChatFragment();
                messageFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, messageFragment).addToBackStack(null).commit();
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
