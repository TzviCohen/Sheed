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

    String firstName;
    String lastName;
    Integer age;
    String imageUrl;
    Gender gender;
    Gender interestedIn;

    List<String> community;


}
