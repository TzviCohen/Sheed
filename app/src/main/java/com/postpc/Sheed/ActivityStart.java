package com.postpc.Sheed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

//check
import com.postpc.Sheed.database.SheedUsersDB;

import static com.postpc.Sheed.Utils.IMAGE_URL_INTENT;


public class ActivityStart extends AppCompatActivity {
    SheedUsersDB db;


    public String emailIn;
    public String passwordIn;
    EditText email;
    EditText password;
    Button sign_in;
    Button sign_up;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        db = SheedApp.getDB();
        context = this;


        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);


        email.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                emailIn = email.getText().toString();
            }
        });


        password.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                passwordIn = password.getText().toString();
            }
        });

        sign_in.setOnClickListener(v -> {
            db.saveUserIdToSP(emailIn);

            Intent signActivityIntent = new Intent(context, MainActivity.class);
            signActivityIntent.putExtra("password", passwordIn);
            startActivity(signActivityIntent);
        });

        sign_up.setOnClickListener(v -> {
            Intent signActivityIntent = new Intent(context, ActivityRegister.class);
            startActivity(signActivityIntent);
        });

//        sign_up.setOnClickListener(v -> {
//            Intent signActivityIntent = new Intent(context, ActivitySignIn.class);
//            startActivity(signActivityIntent);
//
//        });


    }

}
