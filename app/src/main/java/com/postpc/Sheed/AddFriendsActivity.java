package com.postpc.Sheed;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.postpc.Sheed.database.SheedUsersDB;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class AddFriendsActivity extends AppCompatActivity {

    String userInput;
    SheedUsersDB db;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        EditText enterFriendEmailView = findViewById(R.id.enter_root_view);
        Button fabApprove = findViewById(R.id.fab_approve);
        fabApprove.setEnabled(true);
        enterFriendEmailView.setFocusable(true);
        if (db == null)
        {
            db = SheedApp.getDB();
        }

        enterFriendEmailView.setText("");
        // set listener on the input written by the keyboard to the edit-text
        enterFriendEmailView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            public void afterTextChanged(Editable s) {
                // text did change
                String newText = enterFriendEmailView.getText().toString();
                boolean isValidInput = isValidEmailAddress(newText);
                if (!isValidInput)
                {
                    enterFriendEmailView.setError("Please enter a valid email address");
                }
                fabApprove.setEnabled(isValidInput);
                userInput = newText;
            }
        });

        // set click-listener to the button
        fabApprove.setOnClickListener(v -> {
//            Intent intentToOpenService = new Intent(MainActivity.this, CalculateRootsService.class);
            String friendEmail = enterFriendEmailView.getText().toString();
            if (!isValidEmailAddress(friendEmail))
            {
                // Bad input - disable button and exit
                enterFriendEmailView.setError("Please enter a valid email address");
                fabApprove.setEnabled(false);
                return;
            }


            // Good input - parse to long, start activities and set Views states.
            db.downloadUserAndDo(friendEmail, friendUser -> {
                if (friendUser == null)
                {
                    Toast.makeText(SheedApp.instance, "User does not exists in Sheed", Toast.LENGTH_LONG).show();
                }
                else
                {
                    db.setFriends(db.currentSheedUser, friendUser);
                    Toast.makeText(SheedApp.instance, "You and " + friendUser.firstName + " are now friends on Sheed", Toast.LENGTH_LONG).show();
                }
            });

            finish();
        });
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

}
