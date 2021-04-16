package com.xstudioo.noteme;

import android.app.Application;
import android.content.Context;

public class NoteApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    protected NoteApplication(){
        // empty constructor
    }
    public static Context getContext() {
        return mContext;
    }



}
