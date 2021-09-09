package com.postpc.Sheed.yourMatches;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.database.SheedUsersDB;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourMatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourMatchesFragment extends Fragment {

    private SheedUser sheedUser;
    SheedUsersDB db;

    public YourMatchesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatFragment.
     */
    public static YourMatchesFragment newInstance() {
        YourMatchesFragment fragment = new YourMatchesFragment();
        Bundle args = new Bundle();
        // in case you want to pass arguments:
        // args.putSerializable(SHEED_USER, sheedUser);
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
//        // in case you want to get some arguments:
//        if (getArguments() != null) {
//            // sheedUser = (SheedUser) getArguments().getSerializable(SHEED_USER);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_matches, container, false);
        TextView textUserId = view.findViewById(R.id.your_matches_header_title);
        textUserId.setText("user retrieved from database: " + sheedUser.firstName + " " + sheedUser.lastName);
        return view;
    }
}