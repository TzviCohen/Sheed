package com.postpc.Sheed.database;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.postpc.Sheed.ProcessUserInFS;
import com.postpc.Sheed.Query;
import com.postpc.Sheed.SheedUser;

import java.util.concurrent.atomic.AtomicReference;

import static com.postpc.Sheed.Utils.FS_USERS_COLLECTION;
import static com.postpc.Sheed.Utils.SP_KEY_FOR_USER_ID;
import static com.postpc.Sheed.Utils.USER_ID_KEY;

public class SheedUsersDB {

    private final static String TAG = "SheedApp DB";

    Context context;
    FirebaseFirestore fireStoreApp;
    SharedPreferences spForUserId;

    public SheedUser currentSheedUser;

    public SheedUsersDB(Context context) {
        this.context = context;
        this.fireStoreApp = FirebaseFirestore.getInstance();
        spForUserId = context.getSharedPreferences(SP_KEY_FOR_USER_ID, Context.MODE_PRIVATE);
    }


    // TODO : tune this function with Yaheli
    public boolean isUserExists(String userId){
        fireStoreApp.collection(FS_USERS_COLLECTION).document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                currentSheedUser = documentSnapshot.toObject(SheedUser.class);
            }

        });

        return true;
    }

    public void downloadUserAndDo(String userId , ProcessUserInFS processUserInFS ) {

        fireStoreApp.collection(FS_USERS_COLLECTION).document(userId).get().addOnSuccessListener(documentSnapshot -> {
            SheedUser userObj = documentSnapshot.toObject(SheedUser.class);

            // upon download success apply processFunc
            processUserInFS.process(userObj);
        }).
        addOnFailureListener(e -> Log.d(TAG, "downloadAndDo failure" + e.getMessage()));
    }

    public <T> T downloadUserAndQuery(String userId , Query<T> query) {

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

    public void saveUserIdToSP(String userId) {
        SharedPreferences.Editor editor = this.spForUserId.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }

    public void removeUserIdFromSP(String userId) {
        SharedPreferences.Editor editor = this.spForUserId.edit();
        editor.remove(userId);
        editor.apply();
    }
}
