package com.postpc.Sheed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.postpc.Sheed.Utils.USER_INTENT_SERIALIZABLE_KEY;

public class MainActivity extends AppCompatActivity {

    SheedUsersDB db;
    Context context;
    BottomNavigationView bottomNavigationView;

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

            // but for now im going to MatchActivity just for test

            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
            bottomNavigationView.setSelectedItemId(R.id.make_match);
//            Intent matchActivityIntent = new Intent(context, MatchActivity.class);
//            startActivity(matchActivityIntent);
            return;

        }
        else
        {
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
            bottomNavigationView.setSelectedItemId(R.id.make_match);

            db.downloadUserAndDo(userId, sheedUser -> {

                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
//
//                Intent matchActivityIntent = new Intent(context, MatchActivity.class);
//                matchActivityIntent.putExtra(USER_INTENT_SERIALIZABLE_KEY, sheedUser);
//                startActivity(matchActivityIntent);
            });
        }
    }

    ProfileFragment profileFragment = new ProfileFragment();
    ChatFragment chatFragment = new ChatFragment();
    MakeMatchesFragment makeMatchesFragment = new MakeMatchesFragment();


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                            return true;

                        case R.id.chat:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, chatFragment).commit();
                            return true;

                        case R.id.make_matches:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, makeMatchesFragment).commit();
                            return true;
                    }
                    return false;
                }
            };
}