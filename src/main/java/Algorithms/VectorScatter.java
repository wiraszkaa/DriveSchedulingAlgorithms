package Algorithms;

import Request.Request;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.VectorRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.xy.VectorSeries;
import org.jfree.data.xy.VectorSeriesCollection;
import java.awt.*;

public class VectorScatter extends ApplicationFrame {
    private final VectorSeries vectorSeries;
    private Request lastRequest;

    public VectorScatter(String title) {
        super(title);
        vectorSeries = new VectorSeries("Moves");
    }

    public void addToChart(Request request) {
        if (lastRequest != null) {
            vectorSeries.add(lastRequest.position,
                    lastRequest.arrivalTime,
                    request.position - lastRequest.position,
                    request.arrivalTime - lastRequest.arrivalTime);
        }
        lastRequest = request;
    }

    public void showChart() {
        VectorSeriesCollection vectorSeriesCollection = new VectorSeriesCollection();
        vectorSeriesCollection.addSeries(vectorSeries);

        VectorRenderer r = new VectorRenderer();
        r.setSeriesPaint(0, Color.black);

        XYPlot xyPlot = new XYPlot(vectorSeriesCollection, new NumberAxis("Position"), new NumberAxis("Arrival Time"), r);

        JFreeChart chart = new JFreeChart(xyPlot);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(1000, 500));
        setContentPane(chartPanel);

        this.pack();
        UIUtils.centerFrameOnScreen(this);
        this.setVisible(true);
    }
}