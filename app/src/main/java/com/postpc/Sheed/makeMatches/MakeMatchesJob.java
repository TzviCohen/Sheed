package com.postpc.Sheed.makeMatches;

import java.util.UUID;

public class MakeMatchesJob {

    int lastI;
    int lastJ;
    private final Long timeCreated;
    private final int numOfFriends;
    UUID workerId;

    public MakeMatchesJob(int numOfFriends) {
        this.numOfFriends = numOfFriends;
        this.timeCreated = System.currentTimeMillis();
        lastI = 0;
        lastJ = 0;
        workerId = null;
    }

    public void setWorkerId(UUID workerId) {
        this.workerId = workerId;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public int getLastI() {
        return lastI;
    }

    public int getLastJ() {
        return lastJ;
    }
}
