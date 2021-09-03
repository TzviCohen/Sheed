package com.postpc.Sheed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.postpc.Sheed.database.SheedUsersDB;
import com.postpc.Sheed.makeMatches.MakeMatchesFragment;
import com.postpc.Sheed.profile.ProfileFragment;
import com.postpc.Sheed.yourMatches.YourMatchesFragement;

public class MainActivity extends AppCompatActivity {

    SheedUsersDB db;
    Context context;
    BottomNavigationView bottomNavigationView;
    SheedUser sheedUser;
    private final static String TAG = "SheedApp Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            Thread.sleep(1500);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }
        setTheme(R.style.AppTheme);
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
            Log.i(TAG, "user id from sp is null");
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
            Log.i(TAG, "user id from sp: " + userId);
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
            bottomNavigationView.setSelectedItemId(R.id.make_match);

            db.downloadUserAndDo(userId, sheedUser -> {
            // get the user from db and move to make matches screen
                db.currentSheedUser = sheedUser;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, makeMatchesFragment).commit();
            });
        }
    }

    ProfileFragment profileFragment = ProfileFragment.newInstance();
    YourMatchesFragement yourMatchesFragement = YourMatchesFragement.newInstance();
    MakeMatchesFragment makeMatchesFragment = MakeMatchesFragment.newInstance();

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                            return true;

                        case R.id.chat:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, yourMatchesFragement).commit();
                            return true;

                        case R.id.make_matches:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, makeMatchesFragment).commit();
                            return true;
                    }
                    return false;
                }
            };
}