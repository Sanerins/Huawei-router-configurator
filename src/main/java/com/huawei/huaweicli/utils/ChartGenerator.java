package com.huawei.huaweicli.utils;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;

public class ChartGenerator {
    private static ValueAxis axis;

    public static JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Данные в реальном времени",
                "Прошедшее время",
                "Значения",
                dataset);
        chart.setBackgroundPaint(Color.WHITE);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.getRangeAxis().setRange(new Range(-120, 120));
        axis = plot.getDomainAxis();
        axis.setRange(new Range(-1, DiagnosticsHandler.FOR_5_MINS));

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDefaultShapesVisible(false);
            renderer.setDefaultShapesFilled(false);
            renderer.setDrawSeriesLineAsPath(true);
        }

        for (int i = 0; i < 3; i++) {
            r.setSeriesStroke(i, new BasicStroke( 3.0f ) );
        }

        return chart;
    }

    public static ValueAxis getAxis() {
        return axis;
    }
}
