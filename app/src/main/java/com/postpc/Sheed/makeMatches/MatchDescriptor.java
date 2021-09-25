package com.postpc.Sheed.makeMatches;

import android.util.Pair;

import com.google.gson.GsonBuilder;
import com.postpc.Sheed.SheedUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class MatchDescriptor {

    static class MatcherDesc{

        String name;
        String id;

        public MatcherDesc(String name, String id){
            this.name = name;
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MatcherDesc that = (MatcherDesc) o;
            return id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }


    String user1Id;
    String user2Id;

    ArrayList<MatcherDesc> matchers;

    //String matcherId;
    //String matcherName;



    public MatchDescriptor(String user1Id, String user2Id, String matcherId, String matcherName){

        Pair<String, String> orderedStrings = MatchDescriptor.orderAlphabetically(user1Id, user2Id);
        this.user1Id = orderedStrings.first;
        this.user2Id = orderedStrings.second;

        this.matchers = new ArrayList<>();
        this.matchers.clear();
        addMatcher(matcherId, matcherName);
        //this.matcherId = matcherId;
        //this.matcherName = matcherName;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public static MatchDescriptor fromString(String string){
        return new GsonBuilder().create().fromJson(string, MatchDescriptor.class);
    }

    public static List<MatchDescriptor> fromListOfStrings(List<String> strings)
    {
        ArrayList<MatchDescriptor> matchesDescs = new ArrayList<>();
        for (String matchStr : strings){
            matchesDescs.add(MatchDescriptor.fromString(matchStr));
        }
        return matchesDescs;
    }

    public String getMatchedWith(String myId){
        if (user1Id.equals(myId)){
            return user2Id;
        }
        return user1Id;
    }

    public String getMatcherId() {
        return null;
    }

    public String getMatcherName() {
        return null;
    }

    public ArrayList<String>getMatcherNames() {

        ArrayList<String> names = new ArrayList<>();
        for (MatcherDesc matcherDesc : matchers){
            names.add(matcherDesc.name);
        }
        return names;
    }

    public String getMatchersAsString() {
        List<String> names = this.getMatcherNames();

        if (names.size() == 1) {
            return "Matched by " + names.get(0);
        }

        return "Matched by " + names.size() + " Friends";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchDescriptor that = (MatchDescriptor) o;
        return Objects.equals(user1Id, that.user1Id) &&
                Objects.equals(user2Id, that.user2Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1Id, user2Id);
    }

    public void addMatcher(String matcherId, String matcherName){

        final MatcherDesc matcherDesc = new MatcherDesc(matcherName, matcherId);
        if (!matchers.contains(matcherDesc)){
            matchers.add(new MatcherDesc(matcherName, matcherId));
        }
    }

    public String getKey(){
        return getKey(user1Id, user2Id);
    }

    public static String getKey(String str1, String str2){

        Pair<String, String> orderedStrings = MatchDescriptor.orderAlphabetically(str1, str2);
        return orderedStrings.first + "#" + orderedStrings.second;
    }

    private static Pair<String, String> orderAlphabetically(String str1, String str2){

        String first, second;
        if (str1.compareTo(str2) < 0){
            first = str1;
            second = str2;
        }
        else{
            first = str2;
            second = str1;
        }

        return new Pair<>(first, second);
    }

    public static Boolean isLegalMatch(SheedUser user1, SheedUser user2){

        if (user1.equals(user2)) {
            return false;
        }
        return user1.gender == user2.interestedIn &&
                user2.gender == user1.interestedIn;
    }

    public static Pair<String, String> keyToUsersIds(String key){
        String[] ids = key.split("#", 2);
        if (ids.length == 2)
        {
            return new Pair<>(ids[0], ids[1]);
        }
        return null;
    }
}
