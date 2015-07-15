package com.timetrace.example.sound;

import android.os.AsyncTask;
import android.util.Log;

public class RecordAudioTask extends AsyncTask<Integer, Void, Boolean> {
    private static final String TAG = "GONJI RecordAudioTask";

    protected Boolean doInBackground(Integer[] paramArrayOfInteger) {
        AudioClipRecorder localAudioClipRecorder = new AudioClipRecorder(this);
        try {
            localAudioClipRecorder.startRecordingForTime(2000, 8000, 2);
            return Boolean.valueOf(true);
        } catch (IllegalStateException localIllegalStateException) {
            while (true)
                Log.d("GONJI RecordAudioTask", "RecordAudioTask - doInBackground", localIllegalStateException);
        }
    }

    protected void onCancelled() {
        Log.d("GONJI RecordAudioTask", "OnCancelled");
        super.onCancelled();
    }
}

/* Location:           C:\Users\Atomu\Desktop\MonitoringActivity-dex2jar.jar
 * Qualified Name:     com.sound.RecordAudioTask
 * JD-Core Version:    0.6.0
 */