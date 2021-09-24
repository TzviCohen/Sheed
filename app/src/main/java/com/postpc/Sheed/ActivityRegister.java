//package com.postpc.Sheed;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.imageview.ShapeableImageView;
//import com.postpc.Sheed.database.SheedUsersDB;
//
//import static com.postpc.Sheed.Utils.USER_INTENT_SERIALIZABLE_KEY;
//
//public class ActivitySignIn extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
//
//    SheedUser currentUser;
//    SheedUsersDB db;
//
//    String url;
//    public String firstNameIn;
//    public String lastNameIn;
//    public String emailIn;
//    public Integer ageIn;
//    public String imageUrlIn;
//    public Gender genderIn;
//    public Gender interestedIn_In;
//
//    ImageButton img;
//    EditText firstName;
//    EditText lastName;
//    EditText age;
//    EditText email;
//    EditText gender;
//    EditText interestedIn;
//    Button addFriend;
//    Button answer_q;
//    Button sign;
//    ArrayAdapter<CharSequence> adapter;
//    Context context;
//    String ID;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sign_in_activity);
//
//        db = SheedApp.getDB();
//        context = this;
//
//        Intent intentOpenedMe = getIntent();
//        url = intentOpenedMe.getStringExtra("url");
//
//        Spinner spinner = findViewById(R.id.gender);
//        adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);
//
//        Spinner spinner_interestedIn = findViewById(R.id.interestedIn);
//        ArrayAdapter<CharSequence> adapter_interestedIn = ArrayAdapter.createFromResource(this, R.array.interestedIn, android.R.layout.simple_spinner_item);
//        adapter_interestedIn.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
//        spinner_interestedIn.setAdapter(adapter_interestedIn);
//        spinner_interestedIn.setOnItemSelectedListener(this);
//
//
//        firstName = findViewById(R.id.firstName);
//        lastName = findViewById(R.id.lastName);
//        age = findViewById(R.id.age);
////        gender = findViewById(R.id.gender);
////        interestedIn = findViewById(R.id.interestedIn);
//
//        addFriend = findViewById(R.id.add_friends);
//        answer_q = findViewById(R.id.answer_q);
//        sign = findViewById(R.id.sign);
//        img = findViewById(R.id.image);
//        email = findViewById(R.id.email);
//
//        firstName.addTextChangedListener(new TextWatcher() {
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            public void afterTextChanged(Editable s) {
//                firstNameIn = firstName.getText().toString();
//            }
//        });
//
//        lastName.addTextChangedListener(new TextWatcher() {
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            public void afterTextChanged(Editable s) {
//                lastNameIn = lastName.getText().toString();
//            }
//        });
//
//        age.addTextChangedListener(new TextWatcher() {
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//            public void onTextChanged(CharSequence s, int start, int before, int count) { }
//            public void afterTextChanged(Editable s) {
//                ageIn = Integer.parseInt(age.getText().toString());
//            }
//        });
//
//        email.addTextChangedListener(new TextWatcher() {
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            public void afterTextChanged(Editable s) {
//                emailIn = email.getText().toString();
//            }
//        });
//
////
////        email.addTextChangedListener(new TextWatcher() {
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
////            public void onTextChanged(CharSequence s, int start, int before, int count) { }
////            public void afterTextChanged(Editable s) {
////                emailIn = email.getText().toString();
////            }
////        });
//
////        gender.addTextChangedListener(new TextWatcher() {
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////            }
////
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////            }
////
////            public void afterTextChanged(Editable s) {
////                genderIn = gender.getText().toString();
////            }
////        });
//
////        interestedIn.addTextChangedListener(new TextWatcher() {
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////            }
////
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////            }
////
////            public void afterTextChanged(Editable s) {
////                interestedIn_In = interestedIn.getText().toString();
////            }
////        });
//
//        sign.setOnClickListener(v -> {
//            SheedUser sheedUser = new SheedUser(firstNameIn, lastNameIn, ageIn, genderIn, interestedIn_In, url, emailIn);
//            db.addUser(sheedUser);
//
//
//            // TODO: 08/09/2021 open
//
////            Intent profileActivityIntent = new Intent(context, ProfileActivity.class);
////            profileActivityIntent.putExtra(USER_INTENT_SERIALIZABLE_KEY, sheedUser);
////            startActivity(profileActivityIntent);
//
//
//
//
//        });
//
//        img.setOnClickListener(v -> {
//            Intent matchActivityIntent = new Intent(context, ActivityAddPhoto.class);
//            startActivity(matchActivityIntent);
//
////            Intent startActivityIntent = new Intent(context, ProfileActivity.class);
////            matchActivityIntent.putExtra("user", ID);
////            startActivity(matchActivityIntent);
//
//        });
//    }
//
////    public  void uploadImage(){
////
////    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        System.out.println(parent);
//        System.out.println(view);
//        System.out.println(position);
//
//        System.out.println(id);
//
//        if (parent.getId() == R.id.gender){
//            if (position == 1){
//                genderIn = Gender.MAN;
//                System.out.println("first");
//            }
//
//            if (position == 2){
//                genderIn = Gender.WOMAN;
//            }
//
//            if (position == 3){
//                genderIn = Gender.UNDEFINED;
//            }
//        }
//
//        if (parent.getId() == R.id.interestedIn){
//            if (position == 1){
//                interestedIn_In = Gender.MAN;
//            }
//
//            if (position == 2){
//                interestedIn_In = Gender.WOMAN;
//                System.out.println("sec");
//            }
//
//            if (position == 3){
//                interestedIn_In = Gender.UNDEFINED;
//            }
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//
//    }
//}




package com.postpc.Sheed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.postpc.Sheed.database.SheedUsersDB;
import com.squareup.picasso.Picasso;

import static com.postpc.Sheed.Utils.IMAGE_URL_INTENT;

public class ActivityRegister extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SheedUser currentUser;
    SheedUsersDB db;

    String imageUrl;
    public String passwordIn = "";
    public String firstNameIn = "";
    public String lastNameIn = "";
    public String emailIn = "";
    public Integer ageIn = 0;
    public String imageUrlIn;
    public Gender genderIn;
    public Gender interestedIn_In;

    ImageButton img;
    EditText firstName;
    EditText lastName;
    EditText password;
    EditText age;
    EditText email;
    EditText gender;
    EditText interestedIn;
    Button addFriend;
    Button answer_q;
    Button sign;
    ArrayAdapter<CharSequence> adapter;
    Context context;
    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

//        sign.setEnabled(false);

        db = SheedApp.getDB();
        context = this;

        Intent intentOpenedMe = getIntent();
        imageUrl = intentOpenedMe.getStringExtra(IMAGE_URL_INTENT);

        Spinner spinner = findViewById(R.id.gender);
        adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Spinner spinner_interestedIn = findViewById(R.id.interestedIn);
        ArrayAdapter<CharSequence> adapter_interestedIn = ArrayAdapter.createFromResource(this, R.array.interestedIn, android.R.layout.simple_spinner_item);
        adapter_interestedIn.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner_interestedIn.setAdapter(adapter_interestedIn);
        spinner_interestedIn.setOnItemSelectedListener(this);


        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        age = findViewById(R.id.age);

        //addFriend = findViewById(R.id.add_friends);
//        answer_q = findViewById(R.id.answer_q);
        sign = findViewById(R.id.sign);
        sign.setEnabled(false);
        img = findViewById(R.id.image);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        if (imageUrl == null){
            firstName.setVisibility(View.GONE);
            lastName.setVisibility(View.GONE);
            age.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            spinner_interestedIn.setVisibility(View.GONE);
            password.setVisibility(View.GONE);

        } else {
            img.setEnabled(false);
            firstName.setVisibility(View.VISIBLE);
            lastName.setVisibility(View.VISIBLE);
            age.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            spinner_interestedIn.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
        }

        if (imageUrl != null){
            Picasso.with(this).load(imageUrl).centerCrop().fit().into(img);
        }


        password.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                passwordIn = password.getText().toString();
                if (!passwordIn.equals("")){
                    checkTurn();
                } else {
                    sign.setEnabled(false);
                }
            }

        });

        firstName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                firstNameIn = firstName.getText().toString();
//                checkTurn();
                if (!firstNameIn.equals("")){
                    checkTurn();
                } else {
                    sign.setEnabled(false);
                }
            }

        });

        lastName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                lastNameIn = lastName.getText().toString();
//                checkTurn();
                if (!lastNameIn.equals("")){
                    checkTurn();
                } else {
                    sign.setEnabled(false);
                }
            }

        });

        age.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            public void afterTextChanged(Editable s) {
                try {
                    ageIn = Integer.parseInt(age.getText().toString());
                }
                catch (NumberFormatException e){
                    ageIn = 0;
                }

//                checkTurn();
                if (ageIn != 0){
                    checkTurn();
                } else {
                    sign.setEnabled(false);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                emailIn = email.getText().toString();
//                checkTurn();
                if (!emailIn.equals("")){
                    checkTurn();
                } else {
                    sign.setEnabled(false);
                }
            }
        });



        sign.setOnClickListener(v -> {

            SheedUser sheedUser = new SheedUser(firstNameIn, lastNameIn, ageIn, genderIn, interestedIn_In, imageUrl, emailIn, passwordIn);
            db.downloadUserAndDo(sheedUser.email, sheedUser1 -> {

                boolean userAlreadyExists = sheedUser1 != null;
                if (userAlreadyExists)
                {
                    Toast.makeText(context, "The email address " + sheedUser1.email + " has already been used ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    db.addUser(sheedUser);
                    db.saveUserIdToSP(emailIn);
                    Intent mainActivityIntent = new Intent(context, MainActivity.class);
                    mainActivityIntent.putExtra("password", passwordIn);
                    startActivity(mainActivityIntent);

                }
            });
        });

        img.setOnClickListener(v -> {
            Intent activityAddPhoto = new Intent(context, ActivityAddPhoto.class);
            activityAddPhoto.putExtra("check","sign");
            startActivity(activityAddPhoto);


//            firstName.setVisibility(View.VISIBLE);
//            lastName.setVisibility(View.VISIBLE);
//            age.setVisibility(View.VISIBLE);
//            email.setVisibility(View.VISIBLE);
//            spinner.setVisibility(View.VISIBLE);
//            spinner_interestedIn.setVisibility(View.VISIBLE);

            checkTurn();
        });


    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(parent);
        System.out.println(view);
        System.out.println(position);

        System.out.println(id);

        if (parent.getId() == R.id.gender){
            if (position == 0){
                genderIn = null;
            }


            if (position == 1){
                genderIn = Gender.MAN;
            }

            if (position == 2){
                genderIn = Gender.WOMAN;
            }

            if (position == 3){
                genderIn = Gender.UNDEFINED;
            }
        }

        if (parent.getId() == R.id.interestedIn){
            if (position == 0){
                interestedIn_In = null;
            }

            if (position == 1){
                interestedIn_In = Gender.MAN;
            }

            if (position == 2){
                interestedIn_In = Gender.WOMAN;
            }

            if (position == 3){
                interestedIn_In = Gender.UNDEFINED;
            }
        }
        if (genderIn == Gender.MAN | genderIn == Gender.WOMAN| genderIn == Gender.UNDEFINED ){
            checkTurn();
        } else {
            sign.setEnabled(false);
        }

        if (interestedIn_In == Gender.MAN | interestedIn_In == Gender.WOMAN| interestedIn_In == Gender.UNDEFINED ){
            checkTurn();
        } else {
            sign.setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    public void checkTurn(){
        if (!passwordIn.equals("")  &!firstNameIn.equals("") & !lastNameIn.equals("") & !emailIn.equals("") & imageUrl != null & ageIn != 0 & (genderIn == Gender.MAN | genderIn == Gender.WOMAN| genderIn == Gender.UNDEFINED ) & (interestedIn_In == Gender.MAN | interestedIn_In == Gender.WOMAN| interestedIn_In == Gender.UNDEFINED )){
            sign.setEnabled(true);
        }
    }


//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        System.out.println("onSaveInstanceState");
//        System.out.println(firstNameIn);
//        super.onSaveInstanceState(outState);
//        outState.putString("first_mane", firstNameIn);
//
//
//
//
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        System.out.println("onRestoreInstanceState");
//        super.onRestoreInstanceState(savedInstanceState);
//        firstNameIn = savedInstanceState.getString("first_mane");
//        firstName.setText(firstNameIn);
//
//
//
//    }

}