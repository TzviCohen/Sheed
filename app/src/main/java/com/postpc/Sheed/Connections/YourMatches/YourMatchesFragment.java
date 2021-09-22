package com.postpc.Sheed.Connections.YourMatches;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.Utils;
import com.postpc.Sheed.database.SheedUsersDB;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourMatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourMatchesFragment extends Fragment {

    private final static String PAGE_TITLE = "Connections";
    private SheedUser sheedUser;
    TextView page_title;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_matches, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerYourMatchesList);

        List<String> userIdMatches = sheedUser.matches;
//        List<String> matchMadeBy = sheedUser.matchesMade;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            setPageTitle();
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        YourMatchesAdapter adapter = new YourMatchesAdapter(getActivity(), userIdMatches);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void setPageTitle() {
        page_title = getActivity().findViewById(R.id.page_title);
        page_title.setTypeface(Typeface.SANS_SERIF);
        page_title.setText(PAGE_TITLE);
        page_title.setTextSize(24);
        page_title.setTextColor(Color.parseColor(Utils.ALMOST_BLACK));
    }
}