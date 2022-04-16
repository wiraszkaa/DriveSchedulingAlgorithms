package Algorithms;

import Request.Request;
import org.jfree.chart.JFreeChart;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class SCAN implements Algorithm {
    private int currentPosition;
    private boolean isMovingRight;
    private ListIterator<Request> iterator;

    private boolean isCSCAN;

    private AlgorithmParameters ap;

    public SCAN(int startingPosition, boolean isStartingRight) {
        currentPosition = startingPosition;
        isMovingRight = isStartingRight;

        isCSCAN = false;
    }

    public SCAN(int startingPosition, boolean isStartingRight, boolean isCSCAN) {
        this(startingPosition, isStartingRight);
        this.isCSCAN = isCSCAN;
    }

    @Override
    public int start(List<Request> requests, int size) {
        System.out.printf("Starting %sSCAN...%n", isCSCAN ? "C-" : "");
        if (currentPosition > size || currentPosition < 0) {
            System.out.println("Setting Starting Position to 0...");
            currentPosition = 0;
        }
        ap = new AlgorithmParameters(size);
        ap.currentTime = requests.get(0).arrivalTime;
        ap.currentPosition = currentPosition;
        ap.totalMoves = 0;
        ap.completed = 0;
        ap.vs = new VectorScatter(isCSCAN ? "C-SCAN" : "SCAN");
        ap.isMovingRight = isMovingRight;
        ap.isCSCAN = isCSCAN;

        requests.sort(Comparator.comparingInt((o) -> o.position));
        iterator = requests.listIterator();
        while (ap.completed < requests.size()) {
            AlgorithmHelper.move(null, iterator, ap);
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
