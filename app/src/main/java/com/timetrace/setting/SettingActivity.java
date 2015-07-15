package com.timetrace.setting;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.timetrace.app.IndexActivity;
import com.timetrace.app.R;
import com.timetrace.objects.BaseActivity;
import com.timetrace.utils.ViewUtil;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initActionBar();

        findViewById(R.id.rl_setting_app_manager).setOnClickListener(this);
        findViewById(R.id.rl_setting_introduction).setOnClickListener(this);
        findViewById(R.id.rl_setting_feed_back).setOnClickListener(this);
        findViewById(R.id.rl_setting_preference).setOnClickListener(this);
    }

    @Override
    protected void initActionBar() {
        ActionBar actionBar = IndexActivity.actionBar;
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_title);
        title.setText("Setting");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        Intent intent;
        switch (id) {
            case R.id.rl_setting_app_manager:
                intent = new Intent(this, AppManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_introduction:
                intent = new Intent(this, IntroductionActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_preference:
                intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_feed_back:
                EditText editText = new EditText(this);
                editText.setLines(3);
                editText.setGravity(Gravity.START | Gravity.TOP);
                new AlertDialog.Builder(this)
                        .setTitle("HELP US TO DO BETTER")
                        .setView(editText)
                        .setPositiveButton("OK", null)
                        .setNegativeButton("CANCEL", null)
                        .show();
                break;
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_setting, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
