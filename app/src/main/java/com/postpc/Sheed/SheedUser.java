package com.postpc.Sheed;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;
import com.google.gson.GsonBuilder;
import com.postpc.Sheed.makeMatches.MatchDescriptor;

import com.postpc.Sheed.UserStatus;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

enum Gender
{
    MAN,
    WOMAN,
    UNDEFINED
}

public class SheedUser implements Serializable {

    public String firstName;
    public String lastName;
    public Integer age;
    public String imageUrl;
    public Gender gender;
    public Gender interestedIn;


    public List<String> community;
    public HashMap<String, String> matchesMap;
    public HashMap<String, String> matchesMadeMap;
    public HashMap<String, String> pairsToSuggestMap;

    public List<String> matches;
    public List<String> matchesMade;
    public List<String> pairsToSuggest;

    public String id;
    public String email;
    public String password;

    public UserStatus lastStatus;
    //public Timestamp lastMatchingAlgoRun;

    SheedUser(String firstName, String lastName, Integer age, Gender gender, Gender interestedIn, String imageUrl, String email, String password)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.interestedIn = interestedIn;
        this.email = email;
        this.password = password;

        this.imageUrl = imageUrl;

        // think about image is handled

        id = UUID.randomUUID().toString();

        // empty lists
        community = new ArrayList<>();
        matches = new ArrayList<>();
        matchesMade = new ArrayList<>();
        pairsToSuggest = new ArrayList<>();


        matchesMap = new HashMap<>();
        matchesMadeMap = new HashMap<>();
        pairsToSuggestMap = new HashMap<>();

        lastStatus = new UserStatus(community);
        //lastMatchingAlgoRun = Timestamp.now();
    }

    SheedUser()
    {
        this.firstName = "";
        this.lastName = "";
        this.age = 0;
        this.gender = Gender.UNDEFINED;
        this.interestedIn = Gender.UNDEFINED;
        this.imageUrl = "";
        this.email = "user@sheed.com";
        // think about image is handled

        id = UUID.randomUUID().toString();

        // empty lists
        community = new ArrayList<>();
        matches = new ArrayList<>();
        matchesMade = new ArrayList<>();
        pairsToSuggest = new ArrayList<>();

        matchesMap = new HashMap<>();
        matchesMadeMap = new HashMap<>();
        pairsToSuggestMap = new HashMap<>();

        lastStatus = new UserStatus(community);

        //lastMatchingAlgoRun = Timestamp.now();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SheedUser sheedUser = (SheedUser) o;
        return id.equals(sheedUser.id) &&
                email.equals(sheedUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long getTimePassedFromLastAlgoRunMinutes(){
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime lastRun = millisToLocalDateTime(getLastRun());
        return lastRun.until(currentTime, ChronoUnit.MINUTES);
    }

    public long getLastRun(){
        return getLastStatus().lastMatchingAlgoRun.toDate().getTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDateTime millisToLocalDateTime(Long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public UserStatus getLastStatus() {
        return lastStatus;
    }

    public void saveStatus(Timestamp lastRun) {
        if (!lastRun.equals(lastStatus.lastMatchingAlgoRun))
        {
            this.lastStatus = new UserStatus(this.community, lastRun);
        }
    }

    public void setLastStatus(UserStatus lastStatus) {
        this.lastStatus = lastStatus;
    }

}
