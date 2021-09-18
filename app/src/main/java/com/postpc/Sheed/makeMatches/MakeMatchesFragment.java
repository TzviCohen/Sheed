package com.postpc.Sheed.makeMatches;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.os.Handler;
import android.util.Pair;
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
import com.postpc.Sheed.Utils;
import com.postpc.Sheed.database.SheedUsersDB;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.postpc.Sheed.Utils.SECOND;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeMatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeMatchesFragment extends Fragment {

    private final static String TAG = "Sheed MakeMatches Frag";
    private final static int HEADER_APPROVAL_NUM = 4;
    private final static String PAGE_TITLE = "Sheed";

    private SheedUser sheedUser;
    SheedUsersDB db;

    TextView rhsNameView;
    TextView lhsNameView;
    TextView page_title;
    ShapeableImageView rhsImage;
    ShapeableImageView lhsImage;
    ImageButton acceptMatchFab;
    ImageButton declineMatchFab;
    TextView headerView;
    View swipeDetector;
    ImageButton backButton;
    TextView matchesNotFoundView;

    boolean isFirstIterRhs = true;
    boolean isFirstIterLhs = true;
    private int headerApprovalCount = 0;

    Pair<SheedUser, SheedUser> suggestedMatch;



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

        isFirstIterRhs = true;
        isFirstIterLhs = true;
        headerApprovalCount = 0;
        final boolean[] waitingForFS = {true};

        setUsersViewVisibility(GONE);
        onMatchesNotFound();

        final LiveData<HashMap<String, SheedUser>> communityLiveData = db.getCommunityLiveData();
        communityLiveData.observe(getViewLifecycleOwner(), stringSheedUserHashMap -> {
            if (waitingForFS[0]){
                matchLoopExecutorHelper();
                waitingForFS[0] = false;
            }

        });

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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            setPageTitle();
        }
        backButton = getActivity().findViewById(R.id.back_button);
        backButton.setOnClickListener(v->{
            getActivity().onBackPressed();
        });

        rhsImage = view.findViewById(R.id.rhs_img);
        lhsImage = view.findViewById(R.id.lhs_img);

        acceptMatchFab = view.findViewById(R.id.like_button);
        declineMatchFab = view.findViewById(R.id.dislike_button);

        swipeDetector = view.findViewById(R.id.swipe_detector);

        matchesNotFoundView = view.findViewById(R.id.match_not_found_view);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setPageTitle() {
        page_title = getActivity().findViewById(R.id.page_title);
        page_title.setTypeface(getResources().getFont(R.font.milla_cilla));
        page_title.setText(PAGE_TITLE);
        page_title.setTextSize(40);
        page_title.setTextColor(Color.parseColor(Utils.PRIMARY_ORANGE));
    }

    private void fillRhsUser(SheedUser sheedUser)
    {
        rhsNameView.setText(sheedUser.firstName);
        Picasso.with(getContext()).load(sheedUser.imageUrl).centerCrop().fit().into(rhsImage);
        float translationValue = (isFirstIterRhs) ? 0f : 360f;

        rhsImage.setVisibility(View.VISIBLE);
        rhsImage.animate().translationXBy(-translationValue).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showHeader();
                    }
                }).start();

        rhsNameView.setVisibility(View.VISIBLE);
        rhsNameView.animate().translationXBy(-translationValue).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showHeader();
                    }
                }).start();

        isFirstIterRhs = false;

        rhsImage.setVisibility(View.VISIBLE);
        rhsNameView.setVisibility(View.VISIBLE);
        matchesNotFoundView.setVisibility(GONE);
    }

    private void fillLhsUser(SheedUser sheedUser)
    {
        lhsNameView.setText(sheedUser.firstName);
        Picasso.with(getContext()).load(sheedUser.imageUrl).centerCrop().fit().into(lhsImage);

        float translationValue = (isFirstIterLhs) ? 0f : 360f;

        lhsImage.setVisibility(View.VISIBLE);
        lhsImage.animate().translationXBy(translationValue).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showHeader();
                    }
                }).start();

        lhsNameView.setVisibility(View.VISIBLE);
        lhsNameView.animate().translationXBy(translationValue).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showHeader();
                    }
                }).start();

        isFirstIterLhs = false;

        lhsImage.setVisibility(View.VISIBLE);
        lhsNameView.setVisibility(View.VISIBLE);
        matchesNotFoundView.setVisibility(GONE);
    }

    private void matchLoopExecutorHelper(){

        // List<String> matchFound = MatchMaker.makeMatch(currentUser.community);
//        List<String> matchFound = MatchMakerEngine.makeMatch();
//        assert matchFound.size() == 2;  // Assert that two users retrieved from MatchMaker

        suggestedMatch = MatchMakerEngine.getMatchFromWorker();
        if (suggestedMatch == null)
        {
            onMatchesNotFound();
        }
        else
        {
            int delay = (isFirstIterLhs && isFirstIterRhs) ? 0 : 1;
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                fillLhsUser(suggestedMatch.first);
                fillRhsUser(suggestedMatch.second);
            }, delay * SECOND);
        }

        //db.downloadUserAndDo(matchFound.get(0), this::fillLhsUser);
        //db.downloadUserAndDo(matchFound.get(1), this::fillRhsUser);
    }

    private void showHeader(){
        headerApprovalCount++;
        if (headerApprovalCount == HEADER_APPROVAL_NUM)
        {
            headerView.setText("We think it's a great match !");
            headerView.setVisibility(View.VISIBLE);
            headerApprovalCount = 0;
        }

    }

    private void onAcceptMatchAction() {

        // Can add animations

        // Update DB to let users know they have been matched
        if (suggestedMatch != null){
            db.setMatched(suggestedMatch.first, suggestedMatch.second, db.currentSheedUser);
        }
        headerView.setText("Love is in the air !"); // I added this just to see that the listeners indeed work
        roundEndRoutine();
    }

    private void onDeclineMatchAction() {

        // Can add animations

        // Update DB for matching algorithm improvement
        headerView.setText("Better luck next time");  // I added this just to see that the listeners indeed work
        roundEndRoutine();
    }

    private void matchLoopExecutor(Integer delaySecs){

        Handler handler = new Handler();
        handler.postDelayed(this::matchLoopExecutorHelper, delaySecs * SECOND);
    }

    private void roundEndRoutine(){

        rhsNameView.animate().translationXBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rhsNameView.setVisibility(GONE);
                    }
                }).start();

        rhsImage.animate().translationXBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rhsImage.setVisibility(GONE);
                    }
                }).start();

        lhsNameView.animate().translationXBy(-360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        lhsNameView.setVisibility(GONE);
                    }
                }).start();


        lhsImage.animate().translationXBy(-360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        lhsImage.setVisibility(GONE);
                    }
                }).start();

        matchLoopExecutor(1);

    }

    private void onMatchesNotFound(){

        setUsersViewVisibility(GONE);

        matchesNotFoundView.setVisibility(View.VISIBLE);
        matchesNotFoundView.setText("No Matches are available, please add more friends");
    }

    private void setUsersViewVisibility(int status){

        if (status == GONE || status == VISIBLE){
            lhsImage.setVisibility(status);
            lhsNameView.setVisibility(status);
            rhsImage.setVisibility(status);
            rhsNameView.setVisibility(status);
        }

    }
}