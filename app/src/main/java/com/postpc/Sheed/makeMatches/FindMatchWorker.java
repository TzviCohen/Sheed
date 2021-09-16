//package com.postpc.Sheed.makeMatches;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.work.Data;
//import androidx.work.WorkInfo;
//import androidx.work.WorkManager;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import com.postpc.Sheed.SheedApp;
//import com.postpc.Sheed.SheedUser;
//import com.postpc.Sheed.database.SheedUsersDB;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//
//
//public class FindMatchWorker extends Worker {
//    public FindMatchWorker(Context context, WorkerParameters workerParams) {
//        super(context, workerParams);
//    }
//
//    @Override
//
//    public Result doWork() {
//
//        SheedUsersDB db = SheedApp.getDB();
//        db.loadCurrentFriends();
//
//        CombinatoricsUtil<String> util = new CombinatoricsUtil<>();
//
//        Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(n, r);
//        while (iterator.hasNext()) {
//            final int[] combination = iterator.next();
//            System.out.println(Arrays.toString(combination));
//        }
//
//
//
//
//        return null;
//    }
//}
