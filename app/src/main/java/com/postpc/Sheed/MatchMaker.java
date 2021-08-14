package com.postpc.Sheed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MatchMaker {

    static SheedUsersDB db = SheedApp.getDB();

    static Query<Gender> genderQuery = sheedUser -> sheedUser.gender;
    static Query<Gender> interestedInQuery = sheedUser -> sheedUser.interestedIn;

    static List<String> makeMatch(List<String> community)
    {

        Random ran = new Random();
        Integer communitySize = community.size();
        Boolean legalMatch = false;
        String user1Id = null, user2Id = null;

        while (!legalMatch)
        {
            user1Id = community.get(ran.nextInt(communitySize));
            user2Id = community.get(ran.nextInt(communitySize));
            legalMatch = isLegalMatch(user1Id, user2Id);
        }

        return new ArrayList<>(Arrays.asList(user1Id, user2Id));
    }

    static Boolean isLegalMatch(String user1Id, String user2Id){

        if (user1Id.equals(user2Id)) {
            return false;
        }

        Gender user1Gender = db.downloadUserAndQuery(user1Id, genderQuery);
        Gender user2Gender = db.downloadUserAndQuery(user2Id, genderQuery);

        Gender user1interestedIn = db.downloadUserAndQuery(user1Id, interestedInQuery);
        Gender user2interestedIn = db.downloadUserAndQuery(user2Id, interestedInQuery);

        return user1Gender == user2interestedIn && user2Gender == user1interestedIn;
    }

}
