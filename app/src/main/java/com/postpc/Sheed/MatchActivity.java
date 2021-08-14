package com.postpc.Sheed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
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


        // List<String> matchFound = MatchMaker.makeMatch(currentUser.community);
        List<String> matchFound = MatchMaker.makeMatch();
        db = SheedApp.getDB();
        assert matchFound.size() == 2;  // Assert that two users retrieved from MatchMaker
        db.downloadUserAndDo(matchFound.get(0), this::fillLhsUser);
        db.downloadUserAndDo(matchFound.get(1), this::fillRhsUser);
    }

    void fillRhsUser(SheedUser sheedUser)
    {
        rhsNameView.setText(sheedUser.firstName);
        Picasso.with(this).load("https://firebasestorage.googleapis.com/v0/b/sheeddb.appspot.com/o/ron_img.jpg?alt=media&token=764195f6-c42f-491c-8801-7cd79ecf9bc1").into(rhsImage);

    }

    void fillLhsUser(SheedUser sheedUser)
    {
        lhsNameView.setText(sheedUser.firstName);
        Picasso.with(this).load("https://firebasestorage.googleapis.com/v0/b/sheeddb.appspot.com/o/PXL_20210510_140746689.jpg?alt=media&token=ef55933b-1583-4a3c-9072-0e9e11c1bdfd").into(lhsImage);
    }
}

