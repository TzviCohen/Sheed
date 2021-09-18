package com.postpc.Sheed;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.WorkInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.ListenerRegistration;
import com.postpc.Sheed.database.SheedUsersDB;
import com.postpc.Sheed.makeMatches.MakeMatchesFragment;
import com.postpc.Sheed.profile.ProfileFragment;
import com.postpc.Sheed.yourMatches.YourMatchesFragment;

import java.util.List;
import java.util.UUID;

import static com.postpc.Sheed.Utils.USER1_EMAIL;
import static com.postpc.Sheed.Utils.WORKER_LAST_I;
import static com.postpc.Sheed.Utils.WORKER_LAST_J;
import static com.postpc.Sheed.Utils.WORK_MANAGER_TAG;

public class MainActivity extends AppCompatActivity {

    SheedUsersDB db;
    Context context;
    BottomNavigationView bottomNavigationView;
    SheedUser sheedUser;
    private final static String TAG = "SheedApp Main Activity";
    ListenerRegistration currentUserCommunityListener;

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        //TODO: remove the following debugging line:
        //db.saveUserIdToSP("fake@mail");
//        db.removeUserIdFromSP();
        String userId = db.getIdFromSP();
        if (userId == null)
        {
            Log.i(TAG, "user id from sp is null");
            Intent launchActivity = new Intent(context, ActivityStart.class);
            startActivity(launchActivity);
            return;

        }
        else
        {
            Log.i(TAG, "user id from sp: " + userId);
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
            bottomNavigationView.getMenu().getItem(1).setChecked(true);

            db.downloadUserAndDo(userId, sheedUser -> {
            // get the user from db and move to make matches screen
                if (sheedUser == null)
                {
                    Toast.makeText(this, "The user " + userId + " does not exists in App Data Base", Toast.LENGTH_LONG).show();
                    db.removeUserIdFromSP();
                    Intent mainActivity = new Intent(context, MainActivity.class);
                    startActivity(mainActivity);
                }
                else
                {
                    db.currentSheedUser = sheedUser;
                    currentUserCommunityListener = db.listenToCommunityChanges();
                    db.loadCurrentFriends(new ProcessUserList() {
                        @Override
                        public void process(List<SheedUser> sheedUsers) {
                            // do noting - only make sure that list is up to date
                        }
                    });
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, makeMatchesFragment).commit();
                }

            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentUserCommunityListener != null)
        {
            currentUserCommunityListener.remove();
        }
    }

    ProfileFragment profileFragment = ProfileFragment.newInstance();
    YourMatchesFragment yourMatchesFragement = YourMatchesFragment.newInstance();
    MakeMatchesFragment makeMatchesFragment = MakeMatchesFragment.newInstance();

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                            return true;

                        case R.id.make_matches_navigate:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, makeMatchesFragment).commit();
                            return true;

                        case R.id.your_matches_navigate:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, yourMatchesFragement).commit();
                            return true;
                    }
                    return false;
                }
            };


}