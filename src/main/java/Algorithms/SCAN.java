package Algorithms;

import Request.Request;
import org.jfree.chart.JFreeChart;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class SCAN implements Algorithm {
    private int completed;
    private int totalMoves;
    private int currentPosition;
    private int currentTime;
    private int size;
    private boolean isMovingRight;
    private ListIterator<Request> iterator;

    private boolean isCSCAN;
    private int cSCANSwitches;

    private VectorScatter vs;

    public SCAN(int startingPosition, boolean isStartingRight) {
        currentPosition = startingPosition;
        isMovingRight = isStartingRight;

        completed = 0;
        totalMoves = 0;
        currentTime = 0;

        isCSCAN = false;
        cSCANSwitches = 0;
    }

    public SCAN(int startingPosition, boolean isStartingRight, boolean isCSCAN) {
        this(startingPosition, isStartingRight);
        this.isCSCAN = isCSCAN;
    }

    @Override
    public int start(List<Request> requests, int size) {
        vs = new VectorScatter(isCSCAN ? "C-SCAN" : "SCAN");

        System.out.printf("Starting %sSCAN...%n", isCSCAN ? "C-" : "");
        if (currentPosition > size || currentPosition < 0) {
            System.out.println("Setting Starting Position to 0...");
            currentPosition = 0;
        }
        currentTime = requests.get(0).arrivalTime;
        this.size = size;
        requests.sort(Comparator.comparingInt((o) -> o.position));
        iterator = requests.listIterator();

        while (completed < requests.size()) {
            move(null, isMovingRight);
        }
        return totalMoves;
    }

    private void scanMovingHandler(Request request, boolean isMovingRight) {
        boolean isAccepted = false;
        currentTime += Math.abs(currentPosition - request.position);
        if (request.arrivalTime <= currentTime && !request.isCompleted) {
            if (request.position >= currentPosition && isMovingRight) {
                isAccepted = true;
            }
            if (request.position <= currentPosition && !isMovingRight) {
                isAccepted = true;
            }
        }
        if (isAccepted) {
            int movedBy = Math.abs(currentPosition - request.position);
            totalMoves += movedBy;
            currentPosition = request.position;

            request.isCompleted = true;
            vs.addToChart(request);
            completed++;
        }
    }

    private void move(Request lastRequest, boolean isMovingRight) {
        if (isMovingRight) {
            moveRight(lastRequest, isCSCAN);
        } else {
            moveLeft(lastRequest, isCSCAN);
        }
    }

    private void moveLeft(Request lastRequest, boolean isC_SCAN) {
        while (iterator.hasPrevious()) {
            Request request = iterator.previous();
            if (request == lastRequest) {
                break;
            }
            scanMovingHandler(request, false);
        }
        if (!iterator.hasPrevious()) {
            totalMoves += currentPosition;
            currentTime += currentPosition;
            if (!isC_SCAN) {
                this.isMovingRight = true;
                currentPosition = 0;
            } else {
                while(iterator.hasNext()) {
                    iterator.next();
                }
                currentPosition = size;
                cSCANSwitches++;
            }
        }
    }

    private void moveRight(Request lastRequest, boolean isC_SCAN) {
        while (iterator.hasNext()) {
            Request request = iterator.next();
            if (request == lastRequest) {
                break;
            }
            scanMovingHandler(request, true);
        }
        if (!iterator.hasNext()) {
            int movedBy = size - currentPosition;
            totalMoves += movedBy;
            currentTime += movedBy;
            if (!isC_SCAN) {
                this.isMovingRight = false;
                currentPosition = size;
            } else {
                while(iterator.hasPrevious()) {
                    iterator.previous();
                }
                currentPosition = 0;
                cSCANSwitches++;
            }
        }
    }

    @Override
    public void setPriorityRequestsAmount(int amount) {

    }

    @Override
    public JFreeChart getChart() {
        return vs.createChart();
    }
}
