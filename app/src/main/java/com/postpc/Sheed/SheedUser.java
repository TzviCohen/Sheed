package com.postpc.Sheed;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

    public List<String> matches;
    public List<String> matchesMade;
    String id;

    SheedUser(String firstName, String lastName, Integer age, Gender gender, Gender interestedIn)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.interestedIn = interestedIn;

        // think about image is handled

        id = UUID.randomUUID().toString();

        // empty lists
        community = new ArrayList<>();
        matches = new ArrayList<>();
        matchesMade = new ArrayList<>();
    }

}
