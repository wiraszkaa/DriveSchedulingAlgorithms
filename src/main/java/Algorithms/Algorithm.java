package Algorithms;

import Request.Request;
import org.jfree.chart.JFreeChart;

import java.util.List;

public interface Algorithm {
    int start(List<Request> requests, int size);
    void setPriorityRequestsAmount(int amount);
    JFreeChart getChart();
}
