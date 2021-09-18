package com.postpc.Sheed;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
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
    public List<String> matches;
    public List<String> matchesMade;
    public List<String> pairsToSuggest;

    public String id;
    public String email;

    SheedUser(String firstName, String lastName, Integer age, Gender gender, Gender interestedIn, String imageUrl, String email)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.interestedIn = interestedIn;
        this.email = email;

        this.imageUrl = imageUrl;

        // think about image is handled

        id = UUID.randomUUID().toString();

        // empty lists
        community = new ArrayList<>();
        matches = new ArrayList<>();
        matchesMade = new ArrayList<>();
        pairsToSuggest = new ArrayList<>();



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
}
