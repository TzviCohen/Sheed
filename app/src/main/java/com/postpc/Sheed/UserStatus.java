package com.postpc.Sheed;

import com.google.firebase.Timestamp;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserStatus {

    int communitySize;
    public String lastCommunityMemberId;
    public Timestamp lastMatchingAlgoRun;

    public UserStatus(List<String> community){
        setCommunityStatus(community);
        lastMatchingAlgoRun = Timestamp.now();
    }

    public UserStatus(List<String> community, Timestamp lastRun){
        setCommunityStatus(community);
        lastMatchingAlgoRun = lastRun;
    }

    public UserStatus(){
        setCommunityStatus(new ArrayList<>());
        lastMatchingAlgoRun = Timestamp.now();
    }

    public void setCommunityStatus(List<String> community){
        communitySize = community.size();
        if (community.isEmpty())
        {
            lastCommunityMemberId = null;
        }
        else
        {
            lastCommunityMemberId = community.get(communitySize - 1);
        }
    }

    public boolean isCommunityStatusEqual(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStatus userStatus = (UserStatus) o;
        boolean lastCommunityMemberEquals =
                (lastCommunityMemberId == null && userStatus.lastCommunityMemberId == null) ||
                (lastCommunityMemberId.equals(userStatus.lastCommunityMemberId));

        return communitySize == userStatus.communitySize &&
                lastCommunityMemberEquals;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public static com.postpc.Sheed.UserStatus fromString(String string){
        return new GsonBuilder().create().fromJson(string, com.postpc.Sheed.UserStatus.class);
    }
}

