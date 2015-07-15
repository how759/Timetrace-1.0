package com.timetrace.example.sound;

import android.media.AudioRecord;
import android.os.AsyncTask;
import android.util.Log;

public class AudioClipRecorder {
    public static final int FEATURENUM = 1;
    public static final int FULLSIZE = 16000;
    public static final int LOW_PASS = 1;
    public static final int RECORDER_SAMPLERATE_8000 = 8000;
    public static final int WINDOW = 800;
    private static final float ALPHA = 0.1F;
    private static final int DEFAULT_BUFFER_INCREASE_FACTOR = 3;
    private static final String TAG = "GONJI AudioClipRecorder";
    private static boolean continueRecording;
    private static double low_total_result;
    private static float[] mLowPassValue;
    private double[] freq_result;
    private AudioRecord recorder;
    private AsyncTask task;

    public AudioClipRecorder() {
        this.task = null;
    }

    public AudioClipRecorder(AsyncTask paramAsyncTask) {
        this.task = paramAsyncTask;
    }

    public static double getResultValue() {
        return low_total_result;
    }

    static float lowPass(float paramFloat1, float paramFloat2, float paramFloat3) {
        return paramFloat2 * (1.0F - paramFloat3) + paramFloat1 * paramFloat3;
    }

    private double calculAvg(double[] paramArrayOfDouble) {
        double d = 0.0D;
        for (int i = 0; ; i++) {
            if (i >= paramArrayOfDouble.length)
                return d / paramArrayOfDouble.length;
            d += paramArrayOfDouble[i];
        }
    }

    private int determineCalculatedBufferSize(int paramInt1, int paramInt2, int paramInt3) {
        int i = determineMinimumBufferSize(paramInt1, paramInt2);
        if (paramInt2 == 2) ;
        for (int j = paramInt3 * 2; ; j = paramInt3) {
            if (j < i) {
                Log.w("GONJI AudioClipRecorder", "Increasing buffer to hold enough samples " + i + " was: " + j);
                j = i;
            }
            return j;
        }
    }

    private int determineMinimumBufferSize(int paramInt1, int paramInt2) {
        return AudioRecord.getMinBufferSize(paramInt1, 16, paramInt2);
    }

    private boolean doRecording(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        if (paramInt3 == -2) {
            Log.e("GONJI AudioClipRecorder", "Bad encoding value, see logcat");
            return false;
        }
        if (paramInt3 == -1) {
            Log.e("GONJI AudioClipRecorder", "Error creating buffer size");
            return false;
        }
        int i = paramInt3 * paramInt5;
        this.recorder = new AudioRecord(1, paramInt1, 16, paramInt2, i);
        short[] arrayOfShort = new short[paramInt4];
        continueRecording = true;
        Log.d("GONJI AudioClipRecorder", "start recording, recording bufferSize: " + i + " read buffer size: " + paramInt4);
        this.recorder.startRecording();
        while (true) {
            if (!continueRecording) ;
            int j;
            do {
                done();
//                return true;
                j = this.recorder.read(arrayOfShort, 0, paramInt4);
            }
            while ((!continueRecording) || ((this.task != null) && (this.task.isCancelled())));
            mLowPassValue = new float[arrayOfShort.length];
            int k = 0;
            label165:
            if (k >= arrayOfShort.length)
                frequencyCal(paramInt1, mLowPassValue, 1);
            for (int m = 0; ; m++) {
                if (m >= 1) ;
                do {
                    if (j != -3)
                        break;
                    Log.e("GONJI AudioClipRecorder", "error reading: ERROR_INVALID_OPERATION");
//                    break;
                    mLowPassValue[k] = lowPass(arrayOfShort[k], mLowPassValue[k], 0.1F);
                    k++;
                    break;
                }
                while (m == 0);
            }
//            label249:
//            if (j != -2)
//                continue;
//            Log.e("GONJI AudioClipRecorder", "error reading: ERROR_BAD_VALUE");
        }
    }

    public void done() {
        Log.d("GONJI AudioClipRecorder", "shut down recorder");
        if (this.recorder != null) {
            this.recorder.stop();
            this.recorder.release();
            this.recorder = null;
        }
    }

    public void frequencyCal(int paramInt1, float[] paramArrayOfFloat, int paramInt2) {
        this.freq_result = new double[20];
        int i = 0;
        if (i >= paramArrayOfFloat.length) {
            low_total_result = calculAvg(this.freq_result);
            return;
        }
        float[] arrayOfFloat = new float[800];
        int j = arrayOfFloat.length;
        int k = 0;
        int m = 0;
        label48:
        if (m >= 800)
            i += m;
        for (int n = 0; ; n++) {
            if (n >= -1 + arrayOfFloat.length) {
                double d1 = j / paramInt1;
                double d2 = k / 2 / d1;
                this.freq_result[(-1 + i / 800)] = d2;
//                break;
                arrayOfFloat[m] = paramArrayOfFloat[(m + i)];
                m++;
                break;
            }
            if (((arrayOfFloat[n] <= 0.0F) || (arrayOfFloat[(n + 1)] > 0.0F)) && ((arrayOfFloat[n] >= 0.0F) || (arrayOfFloat[(n + 1)] < 0.0F)))
                continue;
            k++;
        }
    }

    public boolean startRecordingForTime(int paramInt1, int paramInt2, int paramInt3) {
        int i = (int) (paramInt1 / 1000.0F * paramInt2);
        return doRecording(paramInt2, paramInt3, determineCalculatedBufferSize(paramInt2, paramInt3, i), i, 3);
    }
}

/* Location:           C:\Users\Atomu\Desktop\MonitoringActivity-dex2jar.jar
 * Qualified Name:     com.sound.AudioClipRecorder
 * JD-Core Version:    0.6.0
 */