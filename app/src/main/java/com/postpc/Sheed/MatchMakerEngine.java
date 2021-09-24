package com.postpc.Sheed;

import android.util.Log;
import android.util.Pair;

import com.postpc.Sheed.database.SheedUsersDB;
import com.postpc.Sheed.makeMatches.MatchDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static com.postpc.Sheed.Utils.USER1_EMAIL;
import static com.postpc.Sheed.Utils.USER1_TEST;
import static com.postpc.Sheed.Utils.USER2_EMAIL;
import static com.postpc.Sheed.Utils.USER2_TEST;
import static com.postpc.Sheed.makeMatches.FindMatchWorker.String2Pair;

public class MatchMakerEngine {

    static SheedUsersDB db = SheedApp.getDB();

    static Query<Gender> genderQuery = sheedUser -> sheedUser.gender;
    static Query<Gender> interestedInQuery = sheedUser -> sheedUser.interestedIn;


    // This is a test make match

    public static Pair<String, String> makeMatch()
    {
        return new Pair<>(USER1_EMAIL, USER2_EMAIL);
    }

    public static Pair<SheedUser, SheedUser> getMatchFromWorker() {
        Queue<String> formattedPairs = new LinkedList<>(db.currentSheedUser.pairsToSuggestMap.keySet());
        String keyMatch = formattedPairs.poll();
        if (keyMatch != null)
        {
            Pair<String, String> pairStr = MatchDescriptor.keyToUsersIds(keyMatch);

            db.currentSheedUser.pairsToSuggestMap.remove(keyMatch);
            db.updateUser(db.currentSheedUser);

            if (pairStr != null && db.userFriendsMap != null)
            {
                SheedUser user1 = db.userFriendsMap.get(pairStr.first);
                SheedUser user2 = db.userFriendsMap.get(pairStr.second);
                return new Pair<>(user1, user2);
            }
            else
            {
                Log.d("MatchEngine", "string2pair method failed");
            }
        }
        return null;
    }

    public static String getRandomKeyMatch(){
        List<String> formattedPairs = new ArrayList<>(db.currentSheedUser.pairsToSuggestMap.keySet());
        if (formattedPairs.isEmpty())
        {
            return null;
        }
        else
        {
            int idx = new Random().nextInt(formattedPairs.size());
            return formattedPairs.get(idx);
        }
    }


    public static Pair<SheedUser, SheedUser> getMatchFromKey(String key) {
        Pair<String, String> pairStr = MatchDescriptor.keyToUsersIds(key);
        if (pairStr != null && db.userFriendsMap != null)
        {
            SheedUser user1 = db.userFriendsMap.get(pairStr.first);
            SheedUser user2 = db.userFriendsMap.get(pairStr.second);
            return new Pair<>(user1, user2);
        }

        return null;
    }
}
