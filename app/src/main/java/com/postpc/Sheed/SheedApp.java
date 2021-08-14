package com.postpc.Sheed;

import android.app.Application;

public class SheedApp  extends Application {

    SheedUsersDB sheedUsersDB = null;
    static SheedApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        sheedUsersDB = new SheedUsersDB(this);
        instance = this;
    }

    //private static SandwichStoreApp getInstance() { return  instance; }
    public static SheedUsersDB getDB() {
        return instance.sheedUsersDB;
    }

}
