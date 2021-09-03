package com.postpc.Sheed.makeMatches;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.postpc.Sheed.MatchMakerEngine;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.database.SheedUsersDB;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.postpc.Sheed.Utils.USER_INTENT_SERIALIZABLE_KEY;

public class MatchActivity extends AppCompatActivity {

    SheedUser currentUser;
    SheedUsersDB db;

    TextView rhsNameView;
    TextView lhsNameView;
    ShapeableImageView rhsImage;
    ShapeableImageView lhsImage;
    ImageButton acceptMatchFab;
    ImageButton declineMatchFab;
    TextView headerView;
    View swipeDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        final Intent sheedUserIntent = getIntent();
        if (sheedUserIntent != null)
        {
            currentUser = (SheedUser) sheedUserIntent.getSerializableExtra(USER_INTENT_SERIALIZABLE_KEY);
        }
//        if (currentUser == null)
//        {
//            return;
//        }

        // load views
        rhsNameView = findViewById(R.id.rhs_name);
        lhsNameView = findViewById(R.id.lhs_name);

        rhsImage = findViewById(R.id.rhs_img);
        lhsImage = findViewById(R.id.lhs_img);

        acceptMatchFab = findViewById(R.id.make_match);
        declineMatchFab = findViewById(R.id.not_make_match);
        headerView = findViewById(R.id.header_title);

        swipeDetector = findViewById(R.id.swipe_detector);

        db = SheedApp.getDB();
        matchLoopExecutorHelper();

        acceptMatchFab.setOnClickListener(v -> onAcceptMatchAction());
        declineMatchFab.setOnClickListener(v -> onDeclineMatchAction());

        acceptMatchFab.bringToFront();
        declineMatchFab.bringToFront();

        swipeDetector.setOnTouchListener(new OnSwipeTouchListener(this) {
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

    void fillRhsUser(SheedUser sheedUser)
    {
        rhsNameView.setText(sheedUser.firstName);
        Picasso.with(this).load(sheedUser.imageUrl).into(rhsImage);

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

    void fillLhsUser(SheedUser sheedUser)
    {

        lhsNameView.setText(sheedUser.firstName);
        Picasso.with(this).load(sheedUser.imageUrl).into(lhsImage);

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

    void matchLoopExecutorHelper(){

        // List<String> matchFound = MatchMaker.makeMatch(currentUser.community);
        List<String> matchFound = MatchMakerEngine.makeMatch();
        assert matchFound.size() == 2;  // Assert that two users retrieved from MatchMaker
        db.downloadUserAndDo(matchFound.get(0), this::fillLhsUser);
        db.downloadUserAndDo(matchFound.get(1), this::fillRhsUser);
        headerView.setText("We think it's a great match !");
        headerView.setVisibility(View.VISIBLE);

    }

    void onAcceptMatchAction() {

        // Can add animations

        // Update DB to let users know they have been matched


        headerView.setText("Love is in the air !"); // I added this just to see that the listeners indeed work
        roundEndRoutine();
    }

    void onDeclineMatchAction() {

        // Can add animations

        // Update DB for matching algorithm improvement
        headerView.setText("Better luck next time");  // I added this just to see that the listeners indeed work
        roundEndRoutine();
    }

    void matchLoopExecutor(Integer x){

        Handler handler = new Handler();
        handler.postDelayed(this::matchLoopExecutorHelper, x*1000);
    }

    void roundEndRoutine(){

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

