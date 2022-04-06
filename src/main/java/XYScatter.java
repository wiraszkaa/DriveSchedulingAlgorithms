import Request.Request;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.*;
import java.util.Collection;

public class XYScatter extends ApplicationFrame {

    public XYScatter(Collection<Request> requests) {
        super("Requests");
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries xySeries = new XYSeries("Normal Request");
        XYSeries xySeries1 = new XYSeries("Priority Request");
        for(Request i: requests) {
            if(i.deadLine == -1) {
                xySeries.add(i.position, i.arrivalTime);
            } else {
                xySeries1.add(i.position, i.arrivalTime);
            }
        }
        xySeriesCollection.addSeries(xySeries);
        xySeriesCollection.addSeries(xySeries1);

        XYDotRenderer xyDotRenderer = new XYDotRenderer();
        xyDotRenderer.setSeriesPaint(0, Color.blue);
        xyDotRenderer.setSeriesPaint(1, Color.red);
        xyDotRenderer.setDotHeight(5);
        xyDotRenderer.setDotWidth(5);

        XYPlot xyPlot = new XYPlot(xySeriesCollection, new NumberAxis("Position"), new NumberAxis("Arrival Time"), xyDotRenderer);
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
