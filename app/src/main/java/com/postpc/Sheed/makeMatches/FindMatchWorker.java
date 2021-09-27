package com.postpc.Sheed.makeMatches;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.postpc.Sheed.SheedApp;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.database.SheedUsersDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


import static com.postpc.Sheed.Utils.WORKER_JOB_END_TIME;
import static com.postpc.Sheed.Utils.WORKER_DIFF_ARRAY;
import static com.postpc.Sheed.Utils.WORKER_LAST_I;
import static com.postpc.Sheed.Utils.WORKER_LAST_J;


public class FindMatchWorker extends Worker {

    SheedUsersDB db;
    final static String PAIR_PATTERN = "%s#%s";
    int lastI;
    int lastJ;
    List<String> diffArrayList;

    public FindMatchWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Result doWork() {

        db = SheedApp.getDB();
        lastI = getInputData().getInt(WORKER_LAST_I, 0);
        lastJ = getInputData().getInt(WORKER_LAST_J, 0);
        String[] diffArray = getInputData().getStringArray(WORKER_DIFF_ARRAY);

        diffArrayList = (diffArray == null) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(diffArray));

        db.loadCurrentFriends(this::generateMatches);
        return Result.success(new Data.Builder().putLong(WORKER_JOB_END_TIME, System.currentTimeMillis()).build());
    }

    private void generateMatches(List<SheedUser> friends){

        //HashSet<String> matches = new HashSet<>(db.currentSheedUser.pairsToSuggest);
        for (int i = lastI; i < friends.size(); i++){
            SheedUser user1 = friends.get(i);

            for (int j = 0; j < diffArrayList.size(); j++)
            {
                SheedUser user2 = db.userFriendsMap.get(diffArrayList.get(j));
                if (user2 == null){
                    Log.d("WORKER", "bad and not understable situation" +
                             "Is " + diffArrayList.get(j) + " friend of " + db.currentSheedUser.email +
                            ".\ndiffList is :\n" + diffArrayList );
                }
                if (user1 == null){
                    Log.d("WORKER", "bad and not understable situation" +
                            "user1 is null" );
                }
                assert user1 != null;
                assert user2 != null;
                String matchKey = MatchDescriptor.getKey(user1.email, user2.email);


                //Pair<SheedUser, SheedUser> optionalMatch = new Pair<>(user1, user2);
                //String pairAsStr = pair2String(optionalMatch);
                if (MatchDescriptor.isLegalMatch(user1, user2)){
                    //String pairAsString = pair2String(optionalMatch);
                    if (!db.currentSheedUser.pairsToSuggestMap.containsKey(matchKey))
                    {
                        MatchDescriptor toSuggest = new MatchDescriptor(user1.email, user2.email, db.currentSheedUser.email, db.currentSheedUser.getFullName());

                        //db.currentSheedUser.pairsToSuggest.add(pair2String(optionalMatch));
                        db.currentSheedUser.pairsToSuggestMap.put(matchKey, toSuggest.toString());
                        db.updateUser(db.currentSheedUser);
                        Log.d("WORKER", "pair was added : " + matchKey);

                    }
                    else{
                        Log.d("WORKER", "pair was not added since already is pairsToSuggest : " + matchKey);
                    }
                }
                else {
                    Log.d("WORKER", "pair was not added since it's not legal : " + matchKey);
                }



            }
        }
    }

    String pair2String(Pair<SheedUser, SheedUser> pair){

        // order the pair alphabetically to avoid the same suggestion twice
        String first, second;
        if (pair.first.email.compareTo(pair.second.email) < 0){
            first = pair.first.email;
            second = pair.second.email;
        }
        else{
            first = pair.second.email;
            second = pair.first.email;
        }
        return String.format(PAIR_PATTERN, first, second);
    }

    public static Pair<String, String> String2Pair(String str){
        String[] ids = str.split("#", 2);
        if (ids.length == 2)
        {
            return new Pair<>(ids[0], ids[1]);
        }
        return null;
    }

}


