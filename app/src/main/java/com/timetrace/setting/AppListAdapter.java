package com.timetrace.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.timetrace.app.R;
import com.timetrace.objects.ApplicationInfo;
import com.timetrace.utils.TimeUtil;

import java.util.List;

/**
 * Created by Atomu on 2014/11/27.
 */
public class AppListAdapter extends ArrayAdapter<ApplicationInfo> {
    private List<ApplicationInfo> applicationInfoList;

    public AppListAdapter(Context context, int resource, List<ApplicationInfo> objects) {
        super(context, resource, objects);
        applicationInfoList = objects;
    }

    public void setApplicationInfoList(List<ApplicationInfo> applicationInfoList) {
        this.applicationInfoList = applicationInfoList;
    }

    @Override
    public int getCount() {
        return applicationInfoList.size();
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return applicationInfoList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_running_app, null);
            if (convertView == null)
                return null;

            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
            holder.name = (TextView) convertView.findViewById(R.id.tv_app_name);
            holder.active = (TextView) convertView.findViewById(R.id.tv_app_active);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.icon.setImageDrawable(getItem(position).getIcon());
        holder.name.setText(getItem(position).getAppName());
        holder.active.setText("active: " + TimeUtil.getRelTimeStr(getItem(position).getActiveTime()));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    private class ViewHolder {
        ImageView icon;
        TextView name;
        TextView active;
    }
}
