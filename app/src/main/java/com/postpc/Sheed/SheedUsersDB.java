package com.postpc.Sheed;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicReference;

import static com.postpc.Sheed.Utils.FS_USERS_COLLECTION;
import static com.postpc.Sheed.Utils.SP_KEY_FOR_USER_ID;
import static com.postpc.Sheed.Utils.USER_ID_KEY;

public class SheedUsersDB {

    Context context;
    FirebaseFirestore fireStoreApp;
    SharedPreferences spForUserId;

    SheedUsersDB(Context context) {
        this.context = context;
        this.fireStoreApp = FirebaseFirestore.getInstance();
        spForUserId = context.getSharedPreferences(SP_KEY_FOR_USER_ID, Context.MODE_PRIVATE);
    }

    void downloadUserAndDo(String userId , ProcessUserInFS processUserInFS ) {

        fireStoreApp.collection(FS_USERS_COLLECTION).document(userId).get().addOnSuccessListener(documentSnapshot -> {
            SheedUser userObj = documentSnapshot.toObject(SheedUser.class);

            // upon download success apply processFunc
            processUserInFS.process(userObj);
        }).
        addOnFailureListener(e -> Log.d("DB", "downloadAndDo failure" + e.getMessage()));
    }

    <T> T downloadUserAndQuery(String userId , Query<T> query) {

        AtomicReference<T> returnValue = new AtomicReference<>();
        fireStoreApp.collection(FS_USERS_COLLECTION).document(userId).get().addOnSuccessListener(documentSnapshot -> {
            SheedUser userObj = documentSnapshot.toObject(SheedUser.class);

            // upon download success apply processFunc
            returnValue.set(query.ask(userObj));
        });
        return returnValue.get();
    }

    private <T>  T getFromSP(String key, Class<T> className) {

        String gsonDescriptor = this.spForUserId.getString(key, null);
        if (gsonDescriptor != null)
        {
            return new Gson().fromJson(gsonDescriptor, className);
        }
        return null;
    }

    public String getIdFromSP(){
        return getFromSP(USER_ID_KEY, String.class);
    }
}
