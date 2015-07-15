
package com.timetrace.analyze;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Sales demo bar chart.
 */
public class BarChartBuilder extends ChartBuilder{


	
	public static GraphicalView execute(Context context, long[] data) {
		XYMultipleSeriesDataset dataset;
		XYMultipleSeriesRenderer renderer;
        System.out.println("barchart");
        dataset = buildDataset(data);
        renderer = buildRenderer(data.length);
        System.out.println("barchart2");
		return ChartFactory.getBarChartView(context, dataset, renderer, Type.DEFAULT);

  }

    private static XYMultipleSeriesRenderer buildRenderer(int length) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        for (int i=0; i<length; ++i){
            XYSeriesRenderer r= new XYSeriesRenderer();
            r.setColor(colors[i]);//为每个bar配置颜色
            renderer.addSeriesRenderer(r);
        }
        renderer.setChartTitle("各部分所占时间");
        renderer.setXTitle("tag");
        renderer.setYTitle("所用时间/小时");
        renderer.setXAxisMin(-2);
        renderer.setXAxisMax(6);
        // renderer.setYAxisMin(0);
        // renderer.setYAxisMax(10);
        renderer.setBackgroundColor(Color.WHITE);//���ñ���ɫ
        renderer.setMarginsColor(Color.WHITE);
        renderer.setApplyBackgroundColor(true);
        renderer.setShowGrid(true);//������ʾ����
        renderer.setBarSpacing(0.01);
        renderer.setBarWidth(50);
        renderer.setXLabels(0);
        renderer.setLabelsTextSize(20);
        renderer.setLegendTextSize(24);
        renderer.setXLabelsAngle(-25);
        //renderer.setPanLimits(new double []{-1,20,0,10});
        //for (int i=0; i<values.length; ++i){
        //	renderer.addXTextLabel(i, apptag[i]);
        //}
        return renderer;
    }

    private static XYMultipleSeriesDataset buildDataset(long values[]) {
        XYMultipleSeriesDataset ds = new XYMultipleSeriesDataset();
        long hour = 60*60*1000;
        for (int i=0; i<values.length; ++i){
            XYSeries series = new XYSeries(apptag[i]);
            series.add(i,values[i]/hour);
            ds.addSeries(series);
        }
        return ds;
    }

}
