package com.postpc.Sheed;

import android.media.Image;

import java.io.Serializable;
import java.util.List;

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


}
