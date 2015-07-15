package com.timetrace.analyze;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.text.DecimalFormat;

public class PieChartBuilder extends ChartBuilder{

	public static GraphicalView execute(Context context, long[] data){
		//初始化
		DefaultRenderer renderer;
		CategorySeries categorySeries;
        categorySeries = buildSeries(data);
        renderer = buildRenderer(data.length);
        return ChartFactory.getPieChartView(context, categorySeries, renderer);

	}

    private static CategorySeries buildSeries(long data[]) {
        CategorySeries s= new CategorySeries("bar");
        DecimalFormat df =new DecimalFormat("#.00");
        //计算比例
        double[] percentage =new double[data.length];
        long total=0;
        for (long item :data){
            total+=item;
        }
        for (int i=0;i<data.length;++i) {
            percentage[i] = (double) data[i] / total * 100;
        }
        for (int i=0; i<data.length; ++i){
            s.add(apptag[i]+df.format(percentage[i])+"%" , percentage[i]);
        }
        return s;
    }

    public static DefaultRenderer buildRenderer(int length) {
		DefaultRenderer renderer = new DefaultRenderer();
		for (int i=0; i<length; ++i){
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setApplyBackgroundColor(true);
		renderer.setLabelsColor(Color.rgb(0x3d, 0x59, 0xab));
		renderer.setChartTitle("不同类型程序活动所占比例");
		renderer.setShowLegend(false);
        renderer.setLabelsTextSize(20);
        renderer.setLegendTextSize(24);
		return renderer;
	}
}