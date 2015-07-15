package com.timetrace.app;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.timetrace.analyze.AnalyzeActivity;
import com.timetrace.monitor.AppInfoProvider;
import com.timetrace.monitor.Monitor;
import com.timetrace.monitor.MonitorActivity;
import com.timetrace.monitor.SensorInfoProvider;
import com.timetrace.objects.BaseTabActivity;
import com.timetrace.setting.SettingActivity;
import com.timetrace.utils.UtilManager;
import com.timetrace.utils.ViewUtil;

/**
 * app的主页面, 是其他页面的parent
 */
public class IndexActivity extends BaseTabActivity {
    public static ActionBar actionBar;

    private final String TAG = "IndexAct";
    private final String TAB0 = "TAB0";
    private final String TAB1 = "TAB1";
    private final String TAB2 = "TAB2";

    private Context context = this;
    private ImageView ivMonitor;
    private ImageView ivAnalyze;
    private ImageView ivSetting;

    private SensorInfoProvider sensorInfoProvider;
    private AppInfoProvider appInfoProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // app 初始化
        Monitor.init(getApplication());

        UtilManager.init();

        initTabs();

        sensorInfoProvider = new SensorInfoProvider();
        appInfoProvider = AppInfoProvider.getInstance();
        System.out.println("start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorInfoProvider.registerListener();
        appInfoProvider.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop");
//        applicationUtil.interrupt();
    }

//    @Override
//    public void finish(){
//        Log.d(TAG, "finish");
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(intent);
//        //super.finish();
//    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        Log.d(TAG, "dispatchKeyEvent");
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            Log.d(TAG, "back");
            if(event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0){
                Log.d(TAG, "return");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }
        else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "pause !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        sensorInfoProvider.unregisterListener();
    }

    private void initUnselected() {
        ivMonitor.setImageResource(R.drawable.tab0_off);
        ivAnalyze.setImageResource(R.drawable.tab1_off);
        ivSetting.setImageResource(R.drawable.tab2_off);
    }

    /**
     * 子页面的切换
     */
    private void initTabs() {
        actionBar = getActionBar();
        ViewUtil.customizeActionBar(actionBar, R.layout.actionbar_index);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

        View idcMonitor = LayoutInflater.from(this).inflate(R.layout.tab_host_index, null);
        View idcAnalyze = LayoutInflater.from(this).inflate(R.layout.tab_host_index, null);
        View idcSetting = LayoutInflater.from(this).inflate(R.layout.tab_host_index, null);

        ivMonitor = (ImageView) idcMonitor.findViewById(R.id.iv_tab_icon);
        ivAnalyze = (ImageView) idcAnalyze.findViewById(R.id.iv_tab_icon);
        ivSetting = (ImageView) idcSetting.findViewById(R.id.iv_tab_icon);

        TabHost.TabSpec tabSpec0 = tabHost.newTabSpec(TAB0).setIndicator(idcMonitor).setContent(new Intent(this, MonitorActivity.class));
        tabHost.addTab(tabSpec0);
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec(TAB1).setIndicator(idcAnalyze).setContent(new Intent(this, AnalyzeActivity.class));
        tabHost.addTab(tabSpec1);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec(TAB2).setIndicator(idcSetting).setContent(new Intent(this, SettingActivity.class));
        tabHost.addTab(tabSpec2);

        initUnselected();
        ivMonitor.setImageResource(R.drawable.tab0_on);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                initUnselected();

                if (TAB0.equals(tabId)) {
                    ivMonitor.setImageResource(R.drawable.tab0_on);
                } else if (TAB1.equals(tabId)) {
                    ivAnalyze.setImageResource(R.drawable.tab1_on);
                } else if (TAB2.equals(tabId)) {
                    ivSetting.setImageResource(R.drawable.tab2_on);
                }
            }
        });
    }

}
