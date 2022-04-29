package Algorithms;

import Request.Request;
import org.jfree.chart.JFreeChart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FCFS implements Algorithm {
    private int currentPosition;
    private int priorityRequestsAmount;
    private AlgorithmParameters ap;

    private PriorityAlgorithms priorityAlgorithms;

    public FCFS(int startingPosition) {
        currentPosition = startingPosition;
        priorityAlgorithms = PriorityAlgorithms.NONE;
    }

    public FCFS(int startingPosition, PriorityAlgorithms priorityAlgorithms) {
        this(startingPosition);
        this.priorityAlgorithms = priorityAlgorithms;
    }

    @Override
    public AlgorithmParameters start(List<Request> requests, int size) {
        String name;
        switch (priorityAlgorithms) {
            default -> name = "FCFS";
            case EDF -> name = "FCFS EDF";
            case FDSCAN -> name = "FCFS FD-SCAN";
        }
        System.out.printf("Starting %s...\n", name);
        if (currentPosition > size || currentPosition < 0) {
            System.out.println("Setting Starting Position to 0...");
            currentPosition = 0;
        }
        ArrayList<Request> requestsCopy = new ArrayList<>();
        ArrayList<Request> priorityRequests = new ArrayList<>();
        if (priorityAlgorithms != PriorityAlgorithms.NONE) {
            for (Request request : requests) {
                if (request.deadLine != -1) {
                    priorityRequests.add(request);
                }
                requestsCopy.add(request);
            }
        }
        if (priorityAlgorithms == PriorityAlgorithms.FDSCAN) {
            requestsCopy.sort(Comparator.comparingInt((o) -> o.position));
        }

        ap = new AlgorithmParameters(size);
        ap.currentTime = requests.get(0).arrivalTime;
        ap.currentPosition = currentPosition;
        ap.totalMoves = 0;
        ap.vs = new VectorScatter(name);
        ap.priorityRequests = priorityRequests;

        int failedCount = 0;
        for (Request request : requests) {
            if (priorityAlgorithms == PriorityAlgorithms.EDF) {
                failedCount += AlgorithmHelper.handleEDF(ap);
            } else if (priorityAlgorithms == PriorityAlgorithms.FDSCAN) {
                AlgorithmHelper.handleFDSCAN(requestsCopy, ap);
            }
            if (!request.isCompleted) {
                if (priorityAlgorithms == PriorityAlgorithms.NONE || request.deadLine == -1) {
                    int movedBy = Math.abs(ap.currentPosition - request.position);
                    ap.currentTime += movedBy;
                    if (request.arrivalTime > ap.currentTime) {
                        ap.currentTime = request.arrivalTime;
                    }
                    ap.totalMoves += movedBy;
                    ap.currentPosition = request.position;
                    request.isCompleted = true;
                    ap.vs.addToChart(request);
                }
            }
        }
        if (priorityAlgorithms == PriorityAlgorithms.FDSCAN) {
           failedCount = (int) requests.stream().filter((o) -> !o.isCompleted).count();
        }
        if (priorityAlgorithms != PriorityAlgorithms.NONE) {
            System.out.printf("Failed %s of %s priority requests%n", failedCount, priorityRequestsAmount);
        }
        return ap;
    }

    @Override
    public void setPriorityRequestsAmount(int amount) {
        priorityRequestsAmount = amount;
    }

    @Override
    public JFreeChart getChart() {
        return ap.vs.createChart();
    }
}
