package com.timetrace.example;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetermingActivity extends Activity implements OnClickListener {
    private static final int RATE = 1;
    Recorder m_recorder = null;
    private Sensor_Listner Altitude;
    private Sensor_Listner accelerometerListener;
    private Date curData;
    private Intent intent = null;
    private boolean isJacket = false;
    private boolean isLeft = false;
    private boolean isRight = false;
    private boolean isStart = false;
    private AlarmManager mManager;
    private String phone_state = null;
    private SimpleDateFormat sdf;
    private TextView selection_tview;
    private SensorManager sensorManager;
    private Button start;
    private String state = null;
    private Button stop;
    private String strCurTime;
    private String sub_name = "SubjectA";

    /**
     * 貌似只是一个震动
     */
    private void setAlarm() {
        Intent localIntent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent localPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, localIntent, 0);
        this.mManager.set(0, System.currentTimeMillis() + 3000, localPendingIntent);
        try {
            Thread.sleep(3000L);
            return;
        } catch (InterruptedException localInterruptedException) {
            localInterruptedException.printStackTrace();
        }
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
                return;
            case 2131230721:
                setAlarm();
                try {
                    Thread.sleep(3000L);
                    this.stop.setEnabled(true);
                    this.start.setEnabled(false);
                    startReadingSensorData();
                    Toast.makeText(this, "All set,  Data Collection Started", Toast.LENGTH_LONG).show();
                    return;
                } catch (InterruptedException localInterruptedException) {
                    while (true)
                        localInterruptedException.printStackTrace();
                }
            case 2131230722:
        }
        this.stop.setEnabled(false);
        this.start.setEnabled(true);
        stopRedingSensorData();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
//    setContentView(2130903040);
        this.intent = getIntent();
        this.isLeft = this.intent.getBooleanExtra("isLeft", false);
        this.isRight = this.intent.getBooleanExtra("isRight", false);
        this.isJacket = this.intent.getBooleanExtra("isJacket", false);
        this.state = this.intent.getStringExtra("state");
        this.sub_name = this.intent.getStringExtra("sub_name");
        this.phone_state = this.intent.getStringExtra("position");
        getWindow().addFlags(128);
//    this.start = ((Button)findViewById(2131230721));
//    this.stop = ((Button)findViewById(2131230722));
//    this.selection_tview = ((TextView)findViewById(2131230720));
        String str = "Position: " + this.phone_state + "\n" + "Activity: " + this.state;
        this.selection_tview.setText(str);
        this.stop.setEnabled(false);
        this.mManager = ((AlarmManager) getSystemService(ALARM_SERVICE));
        this.sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        this.start.setOnClickListener(this);
        this.stop.setOnClickListener(this);
        this.m_recorder = new Recorder(this.state, this.phone_state, this.sub_name);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
//    getMenuInflater().inflate(2131165184, paramMenu);
        return true;
    }

    public void startReadingSensorData() {
        if (!this.isStart) {
            this.sdf = new SimpleDateFormat("MM-dd-HH:mm:ss", Locale.KOREA);
            this.curData = new Date();
            /**
             * Audio ?
             */
            this.m_recorder.setDateTime(this.curData);
            this.strCurTime = this.sdf.format(this.curData);
            /**
             * sensor
             */
            this.accelerometerListener = new Sensor_Listner(this.strCurTime, this.state, this.phone_state, this.sub_name);
            /**
             * accelerometer
             */
            this.sensorManager.registerListener(this.accelerometerListener, this.sensorManager.getDefaultSensor(1), 50000);
            /**
             * tri corder
             */
            this.sensorManager.registerListener(this.accelerometerListener, this.sensorManager.getDefaultSensor(6), 50000);
            this.m_recorder.startRecording();
            this.isStart = true;
        }
    }

    public void stopRedingSensorData() {
        if (this.isStart) {
            this.sensorManager.unregisterListener(this.accelerometerListener);
            this.accelerometerListener.stop();
            this.m_recorder.stopRecording();
            this.isStart = false;
            super.onBackPressed();
        }
    }
}

/* Location:           C:\Users\Atomu\Desktop\MonitoringActivity-dex2jar.jar
 * Qualified Name:     com.example.monitoringactivity.DetermingActivity
 * JD-Core Version:    0.6.0
 */