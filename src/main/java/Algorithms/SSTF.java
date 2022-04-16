package Algorithms;

import Request.Request;
import org.jfree.chart.JFreeChart;

import java.util.ArrayList;
import java.util.List;

public class SSTF implements Algorithm {
    private int currentPosition;

    private AlgorithmParameters ap;

    public SSTF(int startingPosition) {
        currentPosition = startingPosition;
    }

    @Override
    public int start(List<Request> requests, int size) {
        System.out.println("Starting SSTF...");
        if(currentPosition > size || currentPosition < 0) {
            System.out.println("Setting Starting Position to 0...");
            currentPosition = 0;
        }
        ap = new AlgorithmParameters(size);
        ap.currentPosition = currentPosition;
        ap.currentTime = requests.get(0).arrivalTime;
        ap.completed = 0;
        ap.totalMoves = 0;
        ap.vs = new VectorScatter("SSTF");

        int lastRequest = 0;

        List<Request> currentRequests = new ArrayList<>();

        while(ap.completed < requests.size()) {
            lastRequest = RequestAdder.addRequests(requests, currentRequests, lastRequest, ap.currentTime);
            if(currentRequests.size() > 0) {
                Request curr = currentRequests.get(0);
                for(Request i: currentRequests) {
                    if(Math.abs(ap.currentPosition - i.position) < Math.abs(ap.currentPosition - curr.position)) {
                        curr = i;
                    }
                }
                int movedBy = Math.abs(ap.currentPosition - curr.position);
                ap.currentTime += movedBy;
                ap.totalMoves += movedBy;

                ap.currentPosition = curr.position;
                currentRequests.remove(curr);

                curr.isCompleted = true;
                ap.vs.addToChart(curr);
                ap.completed++;
            } else {
                if(lastRequest < requests.size()) {
                    ap.currentTime = requests.get(lastRequest).arrivalTime;
                }
            }
        }
        return ap.totalMoves;
    }

    @Override
    public void setPriorityRequestsAmount(int amount) {

    }

    @Override
    public JFreeChart getChart() {
        return ap.vs.createChart();
    }
}
