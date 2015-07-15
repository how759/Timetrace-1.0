package com.timetrace.setting;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.timetrace.app.R;
import com.timetrace.utils.AppUtil;
import com.timetrace.utils.ViewUtil;

public class IntroductionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        ViewUtil.customizeActionBar(getActionBar(), R.layout.actionbar_index);

        ((TextView) findViewById(R.id.tv_introduction)).setText("\tVersion: " + AppUtil.getVersionName() + ". Thanks for your downloading!\n\n\n" +
                "\tOur app is built on an open-source project, and you can search \"TimeTrace\" on github, all source code available.\n" +
                "\tWe focus on analyzing your behavior, and to answer \"Where does time go?\", by analyzing apps' usage and sensor data via your Android device.\n" +
                "\tYou can totally trust this app letting it run in background collecting low-risk data, and all your private information will never been touched.\n" +
                "\tWe would appreciate it if you can share some data with us by touching the \"UPLOAD\" button, and with your help, we can do better.\n\n\n" +
                "\tThis is a beta-version of the project, your behavior will be categorized into two types: App-Active and Motion-Active.\n" +
                "\t You will know exactly what have you done through the day time, and we will enhance your understanding by giving analyzing information and charts.\n" +
                "\tThis app is vary easy and direct, hope you enjoy your TimeTrace :-)");
    }

}
