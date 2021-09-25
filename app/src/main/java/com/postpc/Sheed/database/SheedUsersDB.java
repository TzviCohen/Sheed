package com.postpc.Sheed.database;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.gson.Gson;
import com.postpc.Sheed.MainActivity;
import com.postpc.Sheed.ProcessUserInFS;
import com.postpc.Sheed.ProcessUserList;
import com.postpc.Sheed.Query;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.UserStatus;
import com.postpc.Sheed.makeMatches.FindMatchWorker;
import com.postpc.Sheed.makeMatches.MakeMatchesJob;
import com.postpc.Sheed.makeMatches.MatchDescriptor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.postpc.Sheed.Utils.ALGO_RUN_INTERVAL_MINS;
import static com.postpc.Sheed.Utils.FS_USERS_COLLECTION;
import static com.postpc.Sheed.Utils.SP_KEY_FOR_USER_ID;
import static com.postpc.Sheed.Utils.USER_ID_KEY;
import static com.postpc.Sheed.Utils.WORKER_DIFF_ARRAY;
import static com.postpc.Sheed.Utils.WORKER_LAST_I;
import static com.postpc.Sheed.Utils.WORKER_LAST_J;
import static com.postpc.Sheed.Utils.WORK_MANAGER_TAG;

public class SheedUsersDB {

    private final static String TAG = "SheedApp DB";

    Context context;
    FirebaseFirestore fireStoreApp;
    SharedPreferences spForUserId;

    public WorkManager workManager;
    public SheedUser currentSheedUser;
    public Map<String, SheedUser> userFriendsMap;
    public List<String> lastSnapshot;

    private LiveData<SheedUser> currentUserLiveData;
    private MutableLiveData<SheedUser> currentUserLiveDataMutable;

    public SheedUsersDB(Context context) {

        this.context = context;
        this.fireStoreApp = FirebaseFirestore.getInstance();
        spForUserId = context.getSharedPreferences(SP_KEY_FOR_USER_ID, Context.MODE_PRIVATE);
        workManager = WorkManager.getInstance(context);

        this.currentUserLiveDataMutable = new MutableLiveData<>();
        this.currentUserLiveData = currentUserLiveDataMutable;


        lastSnapshot = null;
    }

    public LiveData<SheedUser> getCurrentUserLiveData() {
        return currentUserLiveData;
    }

    public void downloadUserAndDo(String userId , ProcessUserInFS processUserInFS ) {
        Log.i(TAG, "download user info of user: " + userId);
        fireStoreApp.collection(FS_USERS_COLLECTION).document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if(!documentSnapshot.exists()){
                processUserInFS.process(null);
            }
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

    public void removeUserIdFromSP() {
        SharedPreferences.Editor editor = this.spForUserId.edit();
        editor.remove(USER_ID_KEY);
        editor.apply();
    }

    public void addUser(SheedUser sheedUser) {
        String userId = sheedUser.email;
        fireStoreApp.collection(FS_USERS_COLLECTION).document(userId).set(sheedUser)
                    .addOnFailureListener(e -> Log.d("DB", "downloadAndDo failure" + e.getMessage()));
    }

    public void addChatMessage(String senderId, String receiverId, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", senderId);
        hashMap.put("receiver", receiverId);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }

    public void updateUser(SheedUser updatedUser){

        fireStoreApp.collection(FS_USERS_COLLECTION).document(updatedUser.email).set(updatedUser);
        if (updatedUser.equals(currentSheedUser)){
            currentSheedUser = updatedUser;
            currentUserLiveDataMutable.setValue(currentSheedUser);
        }
    }

    public void logIn(SheedUser sheedUser){
        saveUserIdToSP(sheedUser.email);
        currentSheedUser = sheedUser;
        currentUserLiveDataMutable.setValue(currentSheedUser);
    }

    public void logOut(){
        removeUserIdFromSP();
        currentSheedUser = null;
        lastSnapshot = null;

        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        fireStoreApp.getApp().getApplicationContext().startActivity(mainActivityIntent);
    }

    public void setFriends(SheedUser user1, SheedUser user2){

        user1.community.add(user2.email);
        user2.community.add(user1.email);

        updateUser(user1);
        updateUser(user2);
    }

    public void setMatched(SheedUser user1, SheedUser user2, SheedUser matcher){

        String matchKey = MatchDescriptor.getKey(user1.email, user2.email);

        if (user1.matchesMap.containsKey(matchKey)) {
            MatchDescriptor existingMatchDescriptor = MatchDescriptor.fromString(user1.matchesMap.get(matchKey));
            existingMatchDescriptor.addMatcher(matcher.email, matcher.getFullName());

            user1.matchesMap.put(matchKey, existingMatchDescriptor.toString());
            user2.matchesMap.put(matchKey, existingMatchDescriptor.toString());

            if (!matcher.matchesMadeMap.containsKey(matchKey)) // don't add it twice
            {
                matcher.matchesMadeMap.put(matchKey, existingMatchDescriptor.toString());
                return;
            }
        }
        else{

            MatchDescriptor matchDescriptor = new MatchDescriptor(user1.email, user2.email, matcher.email,
                    matcher.getFullName());

            String newMatchKey = matchDescriptor.getKey();

            user1.matchesMap.put(newMatchKey, matchDescriptor.toString());
            user2.matchesMap.put(newMatchKey, matchDescriptor.toString());
            matcher.matchesMadeMap.put(newMatchKey, matchDescriptor.toString());
        }

        updateUser(user1);
        updateUser(user2);
        updateUser(matcher);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setMatched(String user1Email, String user2Email){

        downloadUsersAndDo(Arrays.asList(user1Email, user2Email), sheedUsers -> {

            assert sheedUsers.size() == 2;

            SheedUser user1 = null, user2 = null;
            for (SheedUser sheedUser : sheedUsers)
            {
                if (sheedUser.email.equals(user1Email)) {
                    user1 = sheedUser;
                }
                else if (sheedUser.email.equals(user2Email)){
                    user2 = sheedUser;
                }
            }

            assert user1 != null && user2 != null;
            setMatched(user1, user2, currentSheedUser);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void downloadUsersAndDo(List<String> usersIds, ProcessUserList userListProcessor){

        if (usersIds.isEmpty())
        {
            return;
        }
        fireStoreApp.collection(FS_USERS_COLLECTION).whereIn("email", usersIds).
                get().addOnSuccessListener(queryDocumentSnapshots -> {

            List<SheedUser> friendsObj = queryDocumentSnapshots.toObjects(SheedUser.class);
            userListProcessor.process(friendsObj);
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadCurrentFriends(ProcessUserList userListProcessor){

        if (currentSheedUser.community.isEmpty())
        {
            return;
        }
        fireStoreApp.collection(FS_USERS_COLLECTION).whereIn("email", currentSheedUser.community).
                get().addOnSuccessListener(queryDocumentSnapshots -> {


                    List<SheedUser> friendsObj = queryDocumentSnapshots.toObjects(SheedUser.class);
                    userFriendsMap = new HashMap<>();
                    for (SheedUser friend : friendsObj){
                        userFriendsMap.put(friend.getEmail(), friend);
                    }
                    userListProcessor.process(friendsObj);
                });
    }

    private void enqueueJob(MakeMatchesJob makeMatchesJob, List<String> diffArray){

        Long millis = makeMatchesJob.getTimeCreated();
        String[] diff = diffArray.toArray(new String[0]);

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(FindMatchWorker.class).
                setInputData(new Data.Builder()
                        .putInt(WORKER_LAST_I, makeMatchesJob.getLastI())
                        .putStringArray(WORKER_DIFF_ARRAY, diff)
                        .putInt(WORKER_LAST_J, makeMatchesJob.getLastJ()).
                                build()).addTag(WORK_MANAGER_TAG).build();

        makeMatchesJob.setWorkerId(workRequest.getId());

        workManager.enqueueUniqueWork(millis.toString(), ExistingWorkPolicy.REPLACE, workRequest);

    }

    private void enqueueNewJob(List<String> oldCommunity, List<String> newCommunity){

        ArrayList<String> diffArray;
        if (oldCommunity == null) {
            diffArray =  new ArrayList<>(newCommunity);
        }
        else if (oldCommunity.size() > newCommunity.size()) {
            diffArray = new ArrayList<>();
        }
        else{       // community got larger
            diffArray = new ArrayList<>(newCommunity);
            diffArray.removeAll(oldCommunity);
        }
        enqueueJob(new MakeMatchesJob(newCommunity.size()), diffArray);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ListenerRegistration listenToCommunityChanges(){

        if (currentSheedUser == null) { return null; }

        String id = currentSheedUser.email;
        final DocumentReference document = fireStoreApp.collection(FS_USERS_COLLECTION).document(id);
        return document.addSnapshotListener((snapshot, error) -> {

            if (error != null) {
                return;
            }
            if (snapshot == null) {
                return;
            }
            if (!snapshot.exists()) {
                return;
            } else {
                SheedUser updatedUser = snapshot.toObject(SheedUser.class);
                if (updatedUser == null) {
                    return;
                }

                currentSheedUser = updatedUser;
                if (!updatedUser.community.equals(lastSnapshot)){

                    if (lastSnapshot == null){  // App launch - run matching algo only if it wasn't done lately
                        UserStatus currentStatus = new UserStatus(currentSheedUser.community);
                        if (currentStatus.isCommunityStatusEqual(currentSheedUser.getLastStatus())) // if user status wasn't changed from last algo run
                        {                                                                           // then run the algorithm only if enough time passed
                            if (currentSheedUser.getTimePassedFromLastAlgoRunMinutes() < ALGO_RUN_INTERVAL_MINS){
                                return;
                            }

                        }
                    }

                    enqueueNewJob(lastSnapshot, updatedUser.community);
                    lastSnapshot = updatedUser.community;
                    Log.d("DB", "match worker job enqueued");
                    return;
                }


            }
        });
    }



    }
