package com.postpc.Sheed;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_add_friend);

        EditText enterFriendEmailView = findViewById(R.id.enter_root_view);
        TextView fabApprove = findViewById(R.id.fab_approve);
        TextView fabCancel = findViewById(R.id.fab_cancel);
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

        fabCancel.setOnClickListener(v -> {
            finish();
        });

        // set click-listener to the button
        fabApprove.setOnClickListener(v -> {
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

                String toastText;
                if (friendUser == null)
                {
                    toastText =  "User does not exists in Sheed";
                }
                else if (friendUser.email.equals(db.currentSheedUser.email))
                {
                    toastText = "You can't be a friend with yourself";
                }
                else if (db.currentSheedUser.community.contains(friendUser.email))
                {
                    toastText = "You and " + friendUser.firstName + " are already friends on Sheed";
                }
                else
                {
                    db.setFriends(db.currentSheedUser, friendUser);
                    toastText = "You and " + friendUser.firstName + " are now friends on Sheed";
                }
                Toast.makeText(SheedApp.instance, toastText, Toast.LENGTH_LONG).show();
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
