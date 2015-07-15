package com.timetrace.objects;

import android.app.Activity;

import com.timetrace.utils.UtilManager;

/**
 * Created by Atomu on 2014/11/22.
 */
public abstract class BaseActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        UtilManager.init();
        initActionBar();
    }

    protected abstract void initActionBar();
}
