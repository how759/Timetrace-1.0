package com.timetrace.objects;

import android.app.TabActivity;

import com.timetrace.utils.UtilManager;

/**
 * Created by Atomu on 2014/11/22.
 */
public class BaseTabActivity extends TabActivity {
    @Override
    protected void onResume() {
        super.onResume();
        UtilManager.init();
    }

}
