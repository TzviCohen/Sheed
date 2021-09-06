package com.postpc.Sheed.makeMatches;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.MatchMakerEngine;
import com.postpc.Sheed.database.SheedUsersDB;
import com.postpc.Sheed.profile.ProfileFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeMatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeMatchesFragment extends Fragment {

    private final static String TAG = "Sheed MakeMatches Frag";

    private SheedUser sheedUser;
    SheedUsersDB db;

    TextView rhsNameView;
    TextView lhsNameView;
    ShapeableImageView rhsImage;
    ShapeableImageView lhsImage;
    ImageButton acceptMatchFab;
    ImageButton declineMatchFab;
    TextView headerView;
    View swipeDetector;

    public MakeMatchesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MakeMatchesFragment.
     */
    public static MakeMatchesFragment newInstance() {
        MakeMatchesFragment fragment = new MakeMatchesFragment();
        Bundle args = new Bundle();

        // in case you want to pass arguments:
        //args.putSerializable(USER_INTENT_SERIALIZABLE_KEY, sheedUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        matchLoopExecutorHelper();

        acceptMatchFab.setOnClickListener(v -> onAcceptMatchAction());
        declineMatchFab.setOnClickListener(v -> onDeclineMatchAction());

        acceptMatchFab.bringToFront();
        declineMatchFab.bringToFront();

        swipeDetector.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeRight() {
                onAcceptMatchAction();
            }
            @Override
            public void onSwipeLeft() {
                onDeclineMatchAction();
            }
        });
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

//        matchLoopExecutorHelper();
//
//        acceptMatchFab.setOnClickListener(v -> onAcceptMatchAction());
//        declineMatchFab.setOnClickListener(v -> onDeclineMatchAction());
//
//        acceptMatchFab.bringToFront();
//        declineMatchFab.bringToFront();
//
//        swipeDetector.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
//            @Override
//            public void onSwipeRight() {
//                onAcceptMatchAction();
//            }
//            @Override
//            public void onSwipeLeft() {
//                onDeclineMatchAction();
//            }
//        });


//        final Intent sheedUserIntent = getIntent();
//        if (sheedUserIntent != null)
//        {
//            currentUser = (SheedUser) sheedUserIntent.getSerializableExtra(USER_INTENT_SERIALIZABLE_KEY);
//        }
//        // in case you want to get some arguments:
//        if (getArguments() != null) {
//            // sheedUser = (SheedUser) getArguments().getSerializable(SHEED_USER);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_matches, container, false);
        headerView = view.findViewById(R.id.header_title);
        headerView.setText("user retrieved from database: " + sheedUser.firstName + " " + sheedUser.lastName);

        rhsNameView = view.findViewById(R.id.rhs_name);
        lhsNameView = view.findViewById(R.id.lhs_name);

        rhsImage = view.findViewById(R.id.rhs_img);
        lhsImage = view.findViewById(R.id.lhs_img);

        acceptMatchFab = view.findViewById(R.id.make_match);
        declineMatchFab = view.findViewById(R.id.not_make_match);

        swipeDetector = view.findViewById(R.id.swipe_detector);

        return view;
    }

    private void fillRhsUser(SheedUser sheedUser)
    {
        rhsNameView.setText(sheedUser.firstName);
        Picasso.with(getContext()).load(sheedUser.imageUrl).into(rhsImage);

        rhsImage.animate().rotationBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rhsImage.setVisibility(View.VISIBLE);
                    }
                }).start();

        rhsNameView.animate().rotationBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rhsNameView.setVisibility(View.VISIBLE);
                    }
                }).start();
    }

    private void fillLhsUser(SheedUser sheedUser)
    {

        lhsNameView.setText(sheedUser.firstName);
        Picasso.with(getContext()).load(sheedUser.imageUrl).into(lhsImage);

        lhsImage.animate().rotationBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        lhsImage.setVisibility(View.VISIBLE);
                    }
                }).start();

        lhsNameView.animate().rotationBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        lhsNameView.setVisibility(View.VISIBLE);
                    }
                }).start();
    }

    private void matchLoopExecutorHelper(){

        // List<String> matchFound = MatchMaker.makeMatch(currentUser.community);
        List<String> matchFound = MatchMakerEngine.makeMatch();
        assert matchFound.size() == 2;  // Assert that two users retrieved from MatchMaker
        db.downloadUserAndDo(matchFound.get(0), this::fillLhsUser);
        db.downloadUserAndDo(matchFound.get(1), this::fillRhsUser);
        headerView.setText("We think it's a great match !");
        headerView.setVisibility(View.VISIBLE);

    }

    private void onAcceptMatchAction() {

        // Can add animations

        // Update DB to let users know they have been matched


        headerView.setText("Love is in the air !"); // I added this just to see that the listeners indeed work
        roundEndRoutine();
    }

    private void onDeclineMatchAction() {

        // Can add animations

        // Update DB for matching algorithm improvement
        headerView.setText("Better luck next time");  // I added this just to see that the listeners indeed work
        roundEndRoutine();
    }

    private void matchLoopExecutor(Integer x){

        Handler handler = new Handler();
        handler.postDelayed(this::matchLoopExecutorHelper, x*1000);
    }

    private void roundEndRoutine(){

        rhsNameView.animate().rotationBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rhsNameView.setVisibility(View.GONE);
                    }
                }).start();

        rhsImage.animate().rotationBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rhsImage.setVisibility(View.GONE);
                    }
                }).start();

        lhsNameView.animate().rotationBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        lhsNameView.setVisibility(View.GONE);
                    }
                }).start();


        lhsImage.animate().rotationBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        lhsImage.setVisibility(View.GONE);
                    }
                }).start();

        headerView.setVisibility(View.GONE);

        matchLoopExecutor(1);

    }
}