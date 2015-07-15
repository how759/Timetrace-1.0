package com.timetrace.setting;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.timetrace.app.R;
import com.timetrace.monitor.AppInfoProvider;
import com.timetrace.objects.ApplicationInfo;
import com.timetrace.utils.ViewUtil;
import com.timetrace.utils.view.pulltorefresh.PullToRefreshBase;
import com.timetrace.utils.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppManagerActivity extends Activity implements View.OnClickListener {
    private final String TAG = AppManagerActivity.class.getSimpleName();

    private PullToRefreshListView appListView;
    private AppListAdapter appListAdapter;
    private List<ApplicationInfo> applicationInfoList;
    private AppInfoProvider appInfoProvider = AppInfoProvider.getInstance();

    private boolean showRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        initActionBar();

        appListView = (PullToRefreshListView) findViewById(R.id.lv_app_list);
        appListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        appListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new RefreshAppList().execute();
            }
        });
        appListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        applicationInfoList = new ArrayList<ApplicationInfo>();
        appListAdapter = new AppListAdapter(this, R.layout.item_running_app, applicationInfoList);
        appListView.setAdapter(appListAdapter);

        new RefreshAppList().execute();
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        ViewUtil.customizeActionBar(actionBar, R.layout.actionbar_app_manager);
        if (actionBar == null)
            return;

        View layout = actionBar.getCustomView();
        layout.findViewById(R.id.tv_show_running).setOnClickListener(this);
        layout.findViewById(R.id.tv_show_installed).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_show_installed:
                showRunning = false;
                new RefreshAppList().execute();
                break;
            case R.id.tv_show_running:
                showRunning = true;
                new RefreshAppList().execute();
                break;
        }
    }

    private void loadAppList() {
        if (showRunning) {
            applicationInfoList = appInfoProvider.getRunningAppList();
        } else {
            applicationInfoList = appInfoProvider.getInstalledApps();
        }
        appListAdapter.setApplicationInfoList(applicationInfoList);

        Log.d(TAG, "length is " + applicationInfoList.size());
    }

    private class AppComparator implements Comparator<ApplicationInfo> {

        @Override
        public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
            //TODO: 获取到的appList是过滤过的, 因此这里不需要再分情况排序了
            return lhs.getImportance() - rhs.getImportance();
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }

    private class RefreshAppList extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            loadAppList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            appListAdapter.notifyDataSetChanged();
            ListView actualListView = appListView.getRefreshableView();
            registerForContextMenu(actualListView);
            actualListView.setAdapter(appListAdapter);
            appListAdapter.sort(new AppComparator());
            appListView.onRefreshComplete();
        }
    }
}
