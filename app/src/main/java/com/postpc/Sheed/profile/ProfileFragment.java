package com.postpc.Sheed.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.postpc.Sheed.ActivityAddPhoto;
import com.postpc.Sheed.AddFriendsActivity;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.Utils;
import com.postpc.Sheed.database.SheedUsersDB;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private final static String TAG = "SheedApp Profile Frag";

    //private SheedUser sheedUser;
    private final static String PAGE_TITLE = "Profile";
    SheedUsersDB db;
    TextView name;
    TextView matches;
    TextView friends;
    TextView logOutButton;
    TextView page_title;
    ImageButton edit_button;
    ShapeableImageView img;
    Button addFriendsButton;



    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        // in case you want to pass arguments:
        // args.putSerializable(SHEED_USER, sheedUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            // There are no request codes
//                            Intent data = result.getData();
//                            fillRhsUser(sheedUser);
//                        }
//                    }
//                });


        System.out.println("!!!!");
        super.onCreate(savedInstanceState);
        //if(sheedUser == null) {
        if (db == null) {
                db = SheedApp.getDB();
        }
//            sheedUser = db.currentSheedUser;
//            Log.i(TAG, "current user: " + sheedUser);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if(getArguments() != null){
//            String imageUrl = getArguments().getString(IMAGE_URL_INTENT);
//            if (imageUrl != null){
//                db.currentSheedUser.imageUrl = imageUrl;
//                db.updateUser(db.currentSheedUser);
//                fillRhsUser(db.currentSheedUser);
//            }
//
//        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        name = view.findViewById(R.id.name);
        matches = view.findViewById(R.id.matches);
        friends = view.findViewById(R.id.friends);
        logOutButton = view.findViewById(R.id.log_out);
        setPageTitle();
        edit_button = view.findViewById(R.id.edit_button);
        img = view.findViewById(R.id.img);
        addFriendsButton = view.findViewById(R.id.add_friends_profile);

        return view;
    }

    private void setPageTitle() {
        page_title = getActivity().findViewById(R.id.page_title);
        page_title.setTypeface(Typeface.SANS_SERIF);
        page_title.setText(PAGE_TITLE);
        page_title.setTextSize(24);
        page_title.setTextColor(Color.parseColor(Utils.ALMOST_BLACK));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        db.getCurrentUserLiveData().observe(getViewLifecycleOwner(), sheedUser -> {
            if (sheedUser != null){
                fillUserInfo(sheedUser);
            }
        });

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d("here1", "here1");
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.d("here3", "here3");
                            // There are no request codes
                            Intent data = result.getData();
                            fillUserInfo(db.currentSheedUser);
                            //sheedUser = db.currentSheedUser;
                        }
                    }
                });


        fillUserInfo(db.currentSheedUser);
        addFriendsButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddFriendsActivity.class));
        });

        logOutButton.setOnClickListener(v ->{
            db.logOut();
//            db.removeUserIdFromSP();
//            Intent launchActivity = new Intent(getContext(), ActivityStart.class);
//            startActivity(launchActivity);
        });

        edit_button.setOnClickListener(v ->{
//            Intent activityAddPhoto = new Intent(getContext(), ActivityAddPhoto.class);
//            activityAddPhoto.putExtra("check","profile");
//            startActivity(activityAddPhoto);
//            Log.d("bla", "bla!1");
//            fillRhsUser(sheedUser);
//            Log.d("bla", "bla!2");

            openSomeActivityForResult(someActivityResultLauncher);

        });


    }

//    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        // There are no request codes
//                        Intent data = result.getData();
//                        fillRhsUser(sheedUser);
//                    }
//                }
//            });
    public void openSomeActivityForResult(ActivityResultLauncher<Intent> someActivityResultLauncher) {
        Log.d("here2", "here2");
        Intent intent = new Intent(getContext(), ActivityAddPhoto.class);
        intent.putExtra("check","profile");
        someActivityResultLauncher.launch(intent);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1){
//            fillRhsUser(sheedUser);
//        }
//    }



//    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        // There are no request codes
//                        Intent data = result.getData();
//                        fillRhsUser(sheedUser);
//                    }
//                }
//            });
//
//    public void openSomeActivityForResult() {
//        Intent intent = new Intent(getContext(), ActivityAddPhoto.class);
//        intent.putExtra("check","profile");
//        someActivityResultLauncher.launch(intent);
//    }

    void fillUserInfo(SheedUser sheedUser)
    {
        if (sheedUser == null) {return;}

        name.setText(sheedUser.firstName);
        Picasso.with(getContext()).load(sheedUser.imageUrl).centerCrop().fit().into(img);

        matches.setText( sheedUser.matchesMadeMap.size()  + " matches");

        friends.setText(sheedUser.community.size() + " friends");
    }
}



//
//package com.postpc.Sheed;
//
//        import android.animation.Animator;
//        import android.animation.AnimatorListenerAdapter;
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.text.Editable;
//        import android.text.TextWatcher;
//        import android.view.View;
//        import android.widget.EditText;
//        import android.widget.ImageButton;
//        import android.widget.TextView;
//
//
//        import androidx.appcompat.app.AppCompatActivity;
//        import androidx.recyclerview.widget.LinearLayoutManager;
//
//        import com.google.android.material.imageview.ShapeableImageView;
//        import com.squareup.picasso.Picasso;
//
//        import static com.postpc.Sheed.Utils.USER1_TEST;
//        import static com.postpc.Sheed.Utils.USER_INTENT_SERIALIZABLE_KEY;
//
//
//public class ProfileActivity extends AppCompatActivity {
//
//    SheedUser currentUser;
//    SheedUsersDB db;
//
//    TextView name;
//    TextView matches;
//    ImageButton edit_button;
//    ShapeableImageView img;
//
//    String ID;
//
//
//    View swipeDetector;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_prifile);
//
//        final Intent sheedUserIntent = getIntent();
//        if (sheedUserIntent != null)
//        {
//            currentUser = (SheedUser) sheedUserIntent.getSerializableExtra(USER_INTENT_SERIALIZABLE_KEY);
//        }
//
//        Intent intentOpenedMe = getIntent();
//        ID = intentOpenedMe.getStringExtra("user");
//        db = SheedApp.getDB();
//        name = findViewById(R.id.name);
//        matches = findViewById(R.id.matches);
//        edit_button = findViewById(R.id.edit_button);
//        img = findViewById(R.id.img);
//        fillRhsUser(currentUser);
//
//
//    }
//
//

//
//
//
//}