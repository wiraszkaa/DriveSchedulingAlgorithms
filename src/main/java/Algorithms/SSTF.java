package Algorithms;

import Request.Request;
import org.jfree.chart.JFreeChart;

import java.util.ArrayList;
import java.util.List;

public class SSTF implements Algorithm {
    private int currentPosition;

    private VectorScatter vs;

    public SSTF(int startingPosition) {
        currentPosition = startingPosition;
        vs = new VectorScatter("SSTF");
    }

    @Override
    public int start(List<Request> requests, int size) {
        System.out.println("Starting SSTF...");
        if(currentPosition > size || currentPosition < 0) {
            System.out.println("Setting Starting Position to 0...");
            currentPosition = 0;
        }
        int lastRequest = 0;

        int completed = 0;
        int totalMoves = 0;

        int currentTime = requests.get(0).arrivalTime;
        List<Request> currentRequests = new ArrayList<>();

        while(completed < requests.size()) {
            lastRequest = RequestAdder.addRequests(requests, currentRequests, lastRequest, currentTime);
            if(currentRequests.size() > 0) {
                Request curr = currentRequests.get(0);
                for(Request i: currentRequests) {
                    if(Math.abs(currentPosition - i.position) < Math.abs(currentPosition - curr.position)) {
                        curr = i;
                    }
                }
                totalMoves += Math.abs(currentPosition - curr.position);
                currentPosition = curr.position;
                currentRequests.remove(curr);
                currentTime = totalMoves;

                curr.isCompleted = true;
                vs.addToChart(curr);
                completed++;
            } else {
                if(lastRequest < requests.size()) {
                    currentTime = requests.get(lastRequest).arrivalTime;
                }
            }
        }
        return totalMoves;
    }

    @Override
    public void setPriorityRequestsAmount(int amount) {

    }

    @Override
    public JFreeChart getChart() {
        return vs.createChart();
    }
}
