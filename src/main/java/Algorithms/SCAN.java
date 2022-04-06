package Algorithms;

import Request.Request;
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

    private boolean isC_SCAN;

    private VectorScatter vs;

    public SCAN(int startingPosition, boolean isStartingRight) {
        currentPosition = startingPosition;
        isMovingRight = isStartingRight;

        completed = 0;
        totalMoves = 0;
        currentTime = 0;

        isC_SCAN = false;
    }

    public SCAN(int startingPosition) {
        this(startingPosition, true);
        isC_SCAN = true;
    }

    @Override
    public int start(List<Request> requests, int size) {
        // TODO
        vs = new VectorScatter(isC_SCAN ? "C-SCAN" : "SCAN");

        System.out.printf("Starting %sSCAN...%n", isC_SCAN ? "C-" : "");
        if(currentPosition > size || currentPosition < 0) {
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
        vs.showChart();
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
        if(isMovingRight) {
            moveRight(lastRequest);
        } else {
            moveLeft(lastRequest);
        }
    }

    private void moveLeft(Request lastRequest) {
        while (iterator.hasPrevious()) {
            Request request = iterator.previous();
            if (request == lastRequest) {
                break;
            }
            scanMovingHandler(request, false);
        }
        if(!iterator.hasPrevious()) {
            this.isMovingRight = true;
            totalMoves += currentPosition;
            currentTime += currentPosition;
            currentPosition = 0;
        }
    }

    private void moveRight(Request lastRequest) {
        while(iterator.hasNext()) {
            Request request = iterator.next();
            if(request == lastRequest) {
                break;
            }
            scanMovingHandler(request, true);
        }
        if(!iterator.hasNext()) {
            this.isMovingRight = false;
            int movedBy = size - currentPosition;
            totalMoves += movedBy;
            currentTime += movedBy;
            currentPosition = size;
        }
    }

    @Override
    public void setPriorityRequestsAmount(int amount) {

    }
}
