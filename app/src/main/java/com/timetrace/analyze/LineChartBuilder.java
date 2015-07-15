package com.timetrace.analyze;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class LineChartBuilder extends ChartBuilder{
	public static GraphicalView execute(Context context, long[][] data, int scale,int unit){
		XYMultipleSeriesDataset dataset;
		XYMultipleSeriesRenderer renderer;
        dataset = buildDataset(data,scale);
        renderer = buildRenderer(data[0].length,unit);
		return ChartFactory.getLineChartView(context, dataset, renderer);
	}

    private static XYMultipleSeriesRenderer buildRenderer(int length,int unit) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        for (int i=0;i<length;++i){
            XYSeriesRenderer r= new XYSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setLabelsTextSize(20);
        renderer.setLegendTextSize(24);
        if (unit == 0)
            renderer.setYTitle("时间/分钟");
        else
            renderer.setYTitle("时间/小时");
        renderer.setApplyBackgroundColor(true);
        return renderer;
    }

    private static XYMultipleSeriesDataset buildDataset(long[][] data,int scale) {

        XYMultipleSeriesDataset ds = new XYMultipleSeriesDataset();
        for (int j=0;j<data[0].length;++j){
            XYSeries series = new XYSeries(apptag[j]);
            for (int i=0; i<scale; ++i){
                series.add(i,data[i][j]);
            }
            ds.addSeries(series);
        }
        return ds;
    }


}
