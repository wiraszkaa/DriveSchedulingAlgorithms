import Request.Request;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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
        XYSeries xySeries = new XYSeries("Request");
        for(Request i: requests) {
            xySeries.add(i.position, i.arrivalTime);
        }
        xySeriesCollection.addSeries(xySeries);

        JFreeChart chart = ChartFactory.createScatterPlot("Requests", "Position", "Arrival Time", xySeriesCollection);
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
