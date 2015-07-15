package com.timetrace.monitor;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timetrace.app.IndexActivity;
import com.timetrace.app.R;
import com.timetrace.objects.BaseActivity;
import com.timetrace.utils.view.RoundProgressBar;
import com.timetrace.utils.TimeUtil;

/**
 * 第一个页面, 显示粗略的app统计还有运动统计信息
 */
public class MonitorActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = MonitorActivity.class.getSimpleName();
    private final int FLAG_PHONE = 0;
    private int activeFlag = FLAG_PHONE;
    private final int FLAG_MOTION = 1;
    private TextView tvActiveTime, tvActiveTitle, tvTotalTime;
    private long activeTime, totalTime;
    private TextView tvChoosePhone, tvChooseMotion;
    private RoundProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        initActionBar();

        findViewById(R.id.btn_test_print_db).setOnClickListener(this);
        tvActiveTime = (TextView) findViewById(R.id.tv_active_time);
        tvActiveTitle = (TextView) findViewById(R.id.tv_active_title);
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);

        tvChoosePhone = (TextView) findViewById(R.id.tv_choose_phone);
        tvChooseMotion = (TextView) findViewById(R.id.tv_choose_motion);
        tvChoosePhone.setOnClickListener(this);
        tvChooseMotion.setOnClickListener(this);

        //progressBar = (RoundProgressBar) findViewById(R.id.rpb_portion);

        new RefreshTask().execute();


    }

    private void refreshView() {
        tvActiveTime.setText(TimeUtil.getRelTimeStr1(activeTime));
        tvTotalTime.setText(TimeUtil.getRelTimeStr1(totalTime));
        switch (activeFlag) {
            case FLAG_PHONE:
                tvActiveTitle.setText("APP ACTIVE");
                tvChoosePhone.setTextColor(getResources().getColor(R.color.theme_blue));
                tvChooseMotion.setTextColor(getResources().getColor(R.color.theme_gray));
                break;
            case FLAG_MOTION:
                tvActiveTitle.setText("MOTION TIME");
                tvChoosePhone.setTextColor(getResources().getColor(R.color.theme_gray));
                tvChooseMotion.setTextColor(getResources().getColor(R.color.theme_blue));
                break;
        }
//
//        progressBar.setMax(100);
//        progressBar.setProgress((int) (activeTime * 1.0 / totalTime) * 100);
        LinearLayout layout_processbar = (LinearLayout) findViewById(R.id.activity_monitor_processbar);
        final DrawView view = new DrawView(this, (float) (activeTime * 1.0) / totalTime, activeFlag);
        view.setMinimumHeight(500);
        view.setMinimumWidth(300);
        //通知view组件重绘
        view.invalidate();
        layout_processbar.removeAllViews();
        layout_processbar.addView(view);
    }

    @Override
    protected void initActionBar() {
        ActionBar actionBar = IndexActivity.actionBar;
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_title);
        title.setText("Monitor");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_test_print_db:
                break;
            case R.id.tv_choose_phone:
                if (activeFlag != FLAG_PHONE) {
                    activeFlag = FLAG_PHONE;
                    new RefreshTask().execute();
                }
                break;
            case R.id.tv_choose_motion:
                if (activeFlag != FLAG_MOTION) {
                    activeFlag = FLAG_MOTION;
                    new RefreshTask().execute();
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new RefreshTask().execute();
    }

    private class RefreshTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            long current = System.currentTimeMillis();
            if (activeFlag == FLAG_PHONE) {
                activeTime = AppDBHelper.getRunningTime(current);
            } else if (activeFlag == FLAG_MOTION) {
                activeTime = MotionDBHelper.getMotionTime(current);
            }
            //TODO: 异常情况就直接退出

            totalTime = current - TimeUtil.getDayStart(current);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            refreshView();
        }
    }
}
