import Algorithms.Algorithm;
import Algorithms.AlgorithmParameters;
import Request.RequestBuilder;
import org.jfree.chart.ChartPanel;
import javax.management.InvalidAttributeValueException;
import javax.swing.*;
import java.awt.*;

public class DriveSimulation {
    private final RequestBuilder requestBuilder;
    private final JPanel panel;

    public DriveSimulation(RequestBuilder requestBuilder) throws InvalidAttributeValueException {
        this.requestBuilder = requestBuilder;
        panel = new JPanel();

        XYScatter xyScatter = new XYScatter(requestBuilder.create());
        panel.add(new ChartPanel(xyScatter.createChart()));
    }

    public void start(Algorithm algorithm) {
        algorithm.setPriorityRequestsAmount(requestBuilder.getPriorityRequestsAmount());
        long start = System.nanoTime();
        AlgorithmParameters ap = algorithm.start(requestBuilder.createCopy(), requestBuilder.getDriveSize());
        long finish = (System.nanoTime() - start);
        System.out.println("Head moved by " + ap.totalMoves);
        if(ap.isCSCAN) {
            System.out.printf("Head switched %s times\n", ap.cSCANSwitches);
        }
        System.out.println("Simulation took " + ElapsedTimeString.getTime(finish));

        panel.add(new ChartPanel(algorithm.getChart()));
    }

    public void showCharts() {
        JFrame frame = new JFrame("Charts");
        JScrollPane scrollBar = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(scrollBar);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1070, 900));
        frame.pack();
        frame.setVisible(true);
    }
}

