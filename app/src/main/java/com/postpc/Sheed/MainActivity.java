package com.postpc.Sheed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static com.postpc.Sheed.Utils.USER_INTENT_SERIALIZABLE_KEY;

public class MainActivity extends AppCompatActivity {

    SheedUsersDB db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        if (db == null)
        {
            db = SheedApp.getDB();
        }

        String userId = db.getIdFromSP();
        if (userId == null)
        {
            // go to registration page
        }
        db.downloadUserAndDo(userId, sheedUser -> {

            Intent matchActivityIntent = new Intent(context, MatchActivity.class);
            matchActivityIntent.putExtra(USER_INTENT_SERIALIZABLE_KEY, sheedUser);
            startActivity(matchActivityIntent);
        });

    }
}