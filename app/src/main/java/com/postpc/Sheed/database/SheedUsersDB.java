package com.postpc.Sheed.database;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.postpc.Sheed.ProcessUserInFS;
import com.postpc.Sheed.ProcessUserList;
import com.postpc.Sheed.Query;
import com.postpc.Sheed.SheedUser;
import com.postpc.Sheed.makeMatches.FindMatchWorker;
import com.postpc.Sheed.makeMatches.MakeMatchesJob;
import com.postpc.Sheed.makeMatches.MatchDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    private LiveData<HashMap<String, SheedUser>> communityLiveData;
    private MutableLiveData<HashMap<String, SheedUser>> communityLiveDataMutable;

    public SheedUsersDB(Context context) {

        this.context = context;
        this.fireStoreApp = FirebaseFirestore.getInstance();
        spForUserId = context.getSharedPreferences(SP_KEY_FOR_USER_ID, Context.MODE_PRIVATE);
        workManager = WorkManager.getInstance(context);
        this.communityLiveDataMutable = new MutableLiveData<>();
        this.communityLiveData = this.communityLiveDataMutable;

        lastSnapshot = null;
    }

    public LiveData<HashMap<String, SheedUser>> getCommunityLiveData(){
        return this.communityLiveData;
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

    public void removeUserIdFromSP() {
        SharedPreferences.Editor editor = this.spForUserId.edit();
        editor.remove(USER_ID_KEY);
        editor.apply();
    }


    public void addUser(SheedUser sheedUser) {
//        String userId = UUID.randomUUID().toString();
        String userId = sheedUser.email;
//        SheedUser sheedUser = new SheedUser(firstName, lastName, age, gender, interestedIn, imageUrl);
        fireStoreApp.collection(FS_USERS_COLLECTION).document(userId).set(sheedUser).addOnSuccessListener(documentSnapshot -> {

//            SheedUser userObj = documentSnapshot.toObject(SheedUser.class);
//
//            // upon download success apply processFunc
//            processUserInFS.process(userObj);
        }).
                addOnFailureListener(e -> Log.d("DB", "downloadAndDo failure" + e.getMessage()));
    }

    public void updateUser(SheedUser updatedUser){

        fireStoreApp.collection(FS_USERS_COLLECTION).document(updatedUser.email).set(updatedUser);
    }

    public void setFriends(SheedUser user1, SheedUser user2){

        user1.community.add(user2.email);
        user2.community.add(user1.email);

        updateUser(user1);
        updateUser(user2);
    }

    public void setMatched(SheedUser user1, SheedUser user2, SheedUser matcher){

        String descriptorAsStr = new MatchDescriptor(user1.email, user2.email, matcher.email,
                matcher.getFullName()).toString();

        if (user1.matches.contains(descriptorAsStr) || user2.matches.contains(descriptorAsStr) ||
                matcher.matchesMade.contains(descriptorAsStr))
        {
            return;
        }

        user1.matches.add(descriptorAsStr);
        user2.matches.add(descriptorAsStr);
        matcher.matchesMade.add(descriptorAsStr);

        updateUser(user1);
        updateUser(user2);
        updateUser(matcher);
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
                    communityLiveDataMutable.setValue((HashMap<String, SheedUser>) userFriendsMap);
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

        // update SP and notify LiveData
        workManager.enqueueUniqueWork(millis.toString(), ExistingWorkPolicy.KEEP, workRequest);

        //Log.d("DB", "work " + number + " enqueued");
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

    public ListenerRegistration listenToCommunityChanges(){

        if (currentSheedUser == null)
        {
            return null;
        }

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
                else if (!updatedUser.community.equals(lastSnapshot)){
                    currentSheedUser = updatedUser;
                    enqueueNewJob(lastSnapshot, updatedUser.community);
                    lastSnapshot = updatedUser.community;
                    Log.d("DB", "match worker job enqueued");
                    return;
                }

                currentSheedUser = updatedUser;

            }
        });
    }



    }
