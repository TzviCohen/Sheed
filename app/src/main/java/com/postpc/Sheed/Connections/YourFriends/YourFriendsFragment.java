package com.postpc.Sheed.Connections.YourFriends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.Sheed.Connections.YourMatches.YourMatchesFragment;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.database.SheedUsersDB;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.postpc.Sheed.Connections.YourFriends.YourFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourFriendsFragment extends Fragment {

    private final static String PAGE_TITLE = "Connections";
    private SheedUser sheedUser;
    TextView page_title;
    SheedUsersDB db;

    public YourFriendsFragment() {}

    public static com.postpc.Sheed.Connections.YourFriends.YourFriendsFragment newInstance() {
        com.postpc.Sheed.Connections.YourFriends.YourFriendsFragment fragment = new com.postpc.Sheed.Connections.YourFriends.YourFriendsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(sheedUser == null) {
            if (db == null) {
                db = SheedApp.getDB();
            }
            sheedUser = db.currentSheedUser;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_friends, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerYourFriendsList);

        List<String> userIdFriends = sheedUser.community;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        YourFriendsAdapter adapter = new YourFriendsAdapter(getActivity(), userIdFriends);
        recyclerView.setAdapter(adapter);

        return view;
    }
}