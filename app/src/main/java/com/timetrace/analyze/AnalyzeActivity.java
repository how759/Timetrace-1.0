package com.timetrace.analyze;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timetrace.app.IndexActivity;
import com.timetrace.app.R;
import com.timetrace.objects.BaseActivity;
import com.timetrace.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.timetrace.app.R.layout.activity_analyze;
import static com.timetrace.app.R.layout.analyze_activity;
import static com.timetrace.app.R.layout.analyze_motion;
import static com.timetrace.app.R.layout.analyze_phone;

/**
 * 负责统计数据的活动
 */
public class AnalyzeActivity extends BaseActivity implements View.OnClickListener {
    private static final String[] slt_range = {"今天", "7天", "30天"};
    private static final String[] slt_type = {"分布", "比例", "趋势"};
    private ViewPager viewPager;
    private TextView textView_phone, textView_motion, textView_activity;
    private LinearLayout selector;
    private List<View> pages;
    private View page_phone, page_motion, page_activity;
    private Button draw_btn;
    private WheelView wvw_sta_range, wvw_sta_type;
    private long[] statistic_data;
    private long[] real_data;
    private long[][] data_perunit;
    private int statistic_type = 1;
    private int statistic_range = 1;

    private AppAnalyzer appAnalyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_analyze);
        initActionBar();
        initView();


        draw_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pages.clear();
                long current = System.currentTimeMillis();
                long day = 24 * 60 * 60 * 1000;
                long hour = 60 * 60 * 1000;
                long minute = 60 * 1000;
                //根据所选时间段得到统计数据
                if (statistic_range == 1)//1天
                    statistic_data = appAnalyzer.analyze(TimeUtil.getDayStart(current), current);
                else if (statistic_range == 2)//7天
                    statistic_data = appAnalyzer.analyze(current - 7 * day, current);
                else statistic_data = appAnalyzer.analyze(current - 30 * day, current);//30天


                for (int i = 0; i < 5; ++i) {
                    real_data[i] = statistic_data[i + 1];
                }
                //根据所选类型得到需要画的图的类型
                if (statistic_type == 1)//分布
                    pages.add(BarChartBuilder.execute(AnalyzeActivity.this, real_data));
                else if (statistic_type == 2)//比例
                    pages.add(PieChartBuilder.execute(AnalyzeActivity.this, real_data));
                else {//趋势
                    int length;
                    if (statistic_range == 1) {//1天
                        length = (int) ((current - TimeUtil.getDayStart(current)) / hour);
                        data_perunit = new long[length][5];
                        for (int i = 0; i < length; ++i) {
                            statistic_data = appAnalyzer.analyze(TimeUtil.getDayStart(current) + hour * i, TimeUtil.getDayStart(current) + hour * (i + 1));//
                            for (int j = 0; j < 5; ++j)
                                data_perunit[i][j] = statistic_data[j + 1] / minute;
                        }
                        pages.add(LineChartBuilder.execute(AnalyzeActivity.this, data_perunit, length, 0));
                    } else {//7天或30天
                        if (statistic_range == 2) length = 7;
                        else length = 30;
                        data_perunit = new long[length][5];
                        for (int i = 0; i < length; ++i) {
                            statistic_data = appAnalyzer.analyze(current - (length - i) * day, current - (length - i - 1) * day);//
                            for (int j = 0; j < 5; ++j)
                                data_perunit[i][j] = statistic_data[j + 1] / hour;
                        }
                        pages.add(LineChartBuilder.execute(AnalyzeActivity.this, data_perunit, length, 1));
                    }
                }
                pages.add(PieChartBuilder.execute(AnalyzeActivity.this, real_data));
                pages.add(BarChartBuilder.execute(AnalyzeActivity.this, real_data));
                SetAdepter();
            }
        });
        SetAdepter();


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        textView_phone.setTextColor(Color.rgb(0x00, 0xa1, 0xe9));
                        textView_motion.setTextColor(Color.rgb(0x58, 0x58, 0x58));
                        textView_activity.setTextColor(Color.rgb(0x58, 0x58, 0x58));
//                        Toast.makeText(AnalyzeActivity.this, i+"",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        textView_motion.setTextColor(Color.rgb(0x00, 0xa1, 0xe9));
                        textView_phone.setTextColor(Color.rgb(0x58, 0x58, 0x58));
                        textView_activity.setTextColor(Color.rgb(0x58, 0x58, 0x58));
//                        Toast.makeText(AnalyzeActivity.this, i+"",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        textView_activity.setTextColor(Color.rgb(0x00, 0xa1, 0xe9));
                        textView_phone.setTextColor(Color.rgb(0x58, 0x58, 0x58));
                        textView_motion.setTextColor(Color.rgb(0x58, 0x58, 0x58));
//                        Toast.makeText(AnalyzeActivity.this, i+"",Toast.LENGTH_LONG).show();
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void SetAdepter() {
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pages.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return (view == o);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(pages.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pages.get(position));
                return pages.get(position);
            }
        });
    }

    //负责滑动选择的初始化
    private void initView() {
        real_data = new long[5];
        draw_btn = (Button) findViewById(R.id.draw_button);

        wvw_sta_range = (WheelView) findViewById(R.id.analyze_range);
        wvw_sta_range.setOffset(2);
        wvw_sta_range.setItemHeight(24);
        wvw_sta_range.setItems(Arrays.asList(slt_range));
        wvw_sta_range.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                statistic_range = selectedIndex-1;
                System.out.println("index="+selectedIndex);
            }
        });
        wvw_sta_range.startScrollerTask();

        wvw_sta_type = (WheelView) findViewById(R.id.analyze_type);
        wvw_sta_type.setOffset(2);
        wvw_sta_type.setItemHeight(24);
        wvw_sta_type.setItems(Arrays.asList(slt_type));
        wvw_sta_type.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                statistic_type = selectedIndex-1;
                System.out.println("index="+selectedIndex);
            }
        });
        wvw_sta_type.startScrollerTask();


        textView_phone = (TextView) findViewById(R.id.analyze_phone);
        textView_motion = (TextView) findViewById(R.id.analyze_motion);
        textView_activity = (TextView) findViewById(R.id.analyze_activity);

        viewPager = (ViewPager) findViewById(R.id.vp_charts);
        pages = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        page_phone = inflater.inflate(analyze_phone, null);
        page_motion = inflater.inflate(analyze_motion, null);
        page_activity = inflater.inflate(analyze_activity, null);
        pages.add(page_phone);
        pages.add(page_motion);
        pages.add(page_activity);

    }

    @Override
    protected void initActionBar() {
        ActionBar actionBar = IndexActivity.actionBar;
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_title);
        title.setText("Analyze");
    }

    @Override
    public void onClick(View v) {

    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_analyze, menu);
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
