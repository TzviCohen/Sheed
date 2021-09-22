package com.postpc.Sheed.Connections.YourMatches;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.postpc.Sheed.Chat.ChatActivity;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.database.SheedUsersDB;
import com.postpc.Sheed.makeMatches.MatchDescriptor;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        MatchDescriptor matchDescriptor = MatchDescriptor.fromString(matchesDescriptors.get(position));
        String userId = matchDescriptor.getMatchedWith(db.currentSheedUser.email);
        String matchMadeBy = matchDescriptor.getMatcherName();

        HashMap<String, String> matchers = db.currentSheedUser.matchesMap;
        String key1 = db.currentSheedUser.email + "#" + userId;
        String key2 = userId + "#" + db.currentSheedUser.email;

        String value1 = matchers.get(key1);
////////////////////////////////

        db.downloadUserAndDo(userId, sheedUser -> {
            if(sheedUser.equals(null)){
                return;
            }
            holder.userName.setText(sheedUser.firstName);
            holder.userAge.setText(sheedUser.age.toString() + " years old");
            holder.matchMadeBy.setText("Matched by\n" + "NO ONE");
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
        TextView matchMadeBy;
        CircleImageView userImage;
        Button chatButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userAge = itemView.findViewById(R.id.userAge);
            matchMadeBy = itemView.findViewById(R.id.matchMadeBy);
            userImage = itemView.findViewById(R.id.userImage);
        }
    }
}
