package com.postpc.Sheed.makeMatches;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
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
        return Result.success();
    }

    private void generateMatches(List<SheedUser> friends){

        HashSet<String> matches = new HashSet<>(db.currentSheedUser.pairsToSuggest);
        for (int i = lastI; i < friends.size(); i++){
            SheedUser user1 = friends.get(i);
            for (int j = 0; j < diffArrayList.size(); j++)
            {
                Data data = new Data.Builder().putInt(WORKER_LAST_I, i).putInt(WORKER_LAST_J, j).build();
                setProgressAsync(data);

                SheedUser user2 = db.userFriendsMap.get(diffArrayList.get(j));
                Pair<SheedUser, SheedUser> optionalMatch = new Pair<>(user1, user2);
                if (isLegalMatch(optionalMatch)){
                    String pairAsString = pair2String(optionalMatch);
                    if (!matches.contains(pairAsString))
                    {
                        db.currentSheedUser.pairsToSuggest.add(pair2String(optionalMatch));
                        db.updateUser(db.currentSheedUser);
                    }
                }
            }
        }
    }

    static Boolean isLegalMatch(Pair<SheedUser, SheedUser> pair){

        if (pair.first.equals(pair.second)) {
            return false;
        }
        return pair.first.gender == pair.second.interestedIn &&
                pair.second.gender == pair.first.interestedIn;
    }

    List<String> listOfPairs2List(List<Pair<String, String>> listOfPairs)
    {
        List<String> output = new ArrayList<>();
        String pattern = "%s#%s";
        for (Pair<String, String> pair : listOfPairs) {
            output.add(String.format(pattern, pair.first, pair.second));
        }
        return output;
    }

    String pair2String(Pair<SheedUser, SheedUser> pair){
        return String.format(PAIR_PATTERN, pair.first.getEmail(), pair.second.getEmail());
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


