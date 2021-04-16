package com.xstudioo.noteme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ClockReceiver extends BroadcastReceiver {

    private static final String TAG = "ClockReceiver";
    public static final String EXTRA_EVENT_ID = "extra.note.id";
    public static final String EXTRA_EVENT_REMIND_TIME = "extra.note.remind.time";
    public static final String EXTRA_EVENT = "extra.note";
    private SimpleDatabase simpleDatabase = SimpleDatabase.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "闹钟响了", Toast.LENGTH_SHORT).show();
        Log.d("20182005274 ", "onReceive: " + intent.getAction());
        WakeLockUtil.wakeUpAndUnlock();
        postToClockActivity(context, intent);
    }

    private void postToClockActivity(Context context, Intent intent) {
        Intent i = new Intent();
        i.setClass(context, ClockActivity.class);
        i.putExtra(EXTRA_EVENT_ID, intent.getIntExtra(EXTRA_EVENT_ID, -1));
        Note note = simpleDatabase.getNote(intent.getIntExtra(EXTRA_EVENT_ID, -1));
        if (note == null) {
            return;
        }
        i.putExtra(EXTRA_EVENT_REMIND_TIME, intent.getStringExtra(EXTRA_EVENT_REMIND_TIME));
        i.putExtra(EXTRA_EVENT, note);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("20182005274", "口区");
        context.startActivity(i);
    }

    public ClockReceiver() {
        super();
        Log.d(TAG, "ClockReceiver: Constructor");
    }

}
