package com.xstudioo.noteme;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ClockService extends Service {
//    public ClockService() {
//    }

    private static final String TAG = "ClockService";
    public static final String EXTRA_EVENT_ID = "extra.note.id";
    public static final String EXTRA_EVENT_REMIND_TIME = "extra.note.remind.time";
    public static final String EXTRA_EVENT = "extra.note";
    private SimpleDatabase simpleDatabase = SimpleDatabase.getInstance();
    public ClockService() {
        Log.d(TAG, "ClockService: Constructor");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: onStartCommand");
        WakeLockUtil.wakeUpAndUnlock();
        postToClockActivity(getApplicationContext(), intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void postToClockActivity(Context context, Intent intent) {
        Intent i = new Intent();
        i.setClass(context, ClockActivity.class);
        i.putExtra(EXTRA_EVENT_ID, intent.getIntExtra(EXTRA_EVENT_ID, -1));
        Note note =  simpleDatabase.getNote(intent.getIntExtra(EXTRA_EVENT_ID, -1));
        if (note == null) {
            return;
        }
        i.putExtra(EXTRA_EVENT_REMIND_TIME, intent.getStringExtra(EXTRA_EVENT_REMIND_TIME));
        i.putExtra(EXTRA_EVENT, note);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
