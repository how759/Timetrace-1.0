package com.timetrace.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context paramContext, Intent paramIntent) {
        ((Vibrator) paramContext.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(2000L);
        Log.v("RECEIVER", "VIBRATE");
    }
}

/* Location:           C:\Users\Atomu\Desktop\MonitoringActivity-dex2jar.jar
 * Qualified Name:     com.example.monitoringactivity.AlarmBroadcastReceiver
 * JD-Core Version:    0.6.0
 */