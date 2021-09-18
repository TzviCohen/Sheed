package com.postpc.Sheed.makeMatches;

import com.google.gson.GsonBuilder;

import java.util.Objects;

public class MatchDescriptor {

    String user1Id;
    String user2Id;
    String matcherId;
    String matcherName;

    public MatchDescriptor(String user1Id, String user2Id, String matcherId, String matcherName){

        if (user1Id.compareTo(user2Id) < 0){
            this.user1Id = user1Id;
            this.user2Id = user2Id;
        }
        else{
            this.user1Id = user2Id;
            this.user2Id = user1Id;
        }

        this.matcherId = matcherId;
        this.matcherName = matcherName;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public static MatchDescriptor fromString(String string){
        return new GsonBuilder().create().fromJson(string, MatchDescriptor.class);
    }

    public String getMatchedWith(String myId){
        if (user1Id.equals(myId)){
            return user2Id;
        }
        return user1Id;
    }

    public String getMatcherId() {
        return matcherId;
    }

    public String getMatcherName() {
        return matcherName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchDescriptor that = (MatchDescriptor) o;
        return Objects.equals(user1Id, that.user1Id) &&
                Objects.equals(user2Id, that.user2Id) &&
                Objects.equals(matcherId, that.matcherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1Id, user2Id, matcherId);
    }
}
