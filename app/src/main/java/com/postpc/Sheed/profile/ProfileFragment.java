package com.postpc.Sheed.profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.postpc.Sheed.R;
import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.database.SheedUsersDB;
import com.squareup.picasso.Picasso;

import static com.postpc.Sheed.Utils.USER_INTENT_SERIALIZABLE_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private final static String TAG = "SheedApp Profile Frag";

    private SheedUser sheedUser;
    SheedUsersDB db;
    TextView name;
    TextView matches;
    ImageButton edit_button;
    ShapeableImageView img;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        name = view.findViewById(R.id.name);
        matches = view.findViewById(R.id.matches);
        edit_button = view.findViewById(R.id.edit_button);
        img = view.findViewById(R.id.img);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillRhsUser(sheedUser);
    }

    void fillRhsUser(SheedUser sheedUser)
    {
        name.setText(sheedUser.firstName);
        Picasso.with(getContext()).load(sheedUser.imageUrl).into(img);

        img.animate().rotationBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        img.setVisibility(View.VISIBLE);
                    }
                }).start();

        img.animate().rotationBy(360f).alpha(1 / 0.3f).setDuration(500L).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        img.setVisibility(View.VISIBLE);
                    }
                }).start();

        matches.setText( sheedUser.matchesMade.size()  + "\nmatches");
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
