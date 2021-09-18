package com.postpc.Sheed.yourMatches;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.postpc.Sheed.Query;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.database.SheedUsersDB;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourMatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourMatchesFragment extends Fragment {

    private SheedUser sheedUser;
    SheedUsersDB db;

    public YourMatchesFragment() {}

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

        Log.i("User Name", sheedUser.firstName + " " + sheedUser.lastName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_matches, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerYourMatchesList);

        List<String> userIdMatches = sheedUser.matches;
        //List<String> matchMadeBy = sheedUser.matchesMade;

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        YourMatchesAdapter adapter = new YourMatchesAdapter(getActivity(), userIdMatches);
        recyclerView.setAdapter(adapter);

        return view;
    }
}