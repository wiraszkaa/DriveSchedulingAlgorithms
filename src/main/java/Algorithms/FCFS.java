package Algorithms;

import Request.Request;
import org.jfree.chart.JFreeChart;

import java.util.ArrayList;
import java.util.List;

public class FCFS implements Algorithm {
    private int currentPosition;
    private int priorityRequestsAmount;

    private boolean isEDFEnabled = false;

    private VectorScatter vs;

    public FCFS(int startingPosition) {
        currentPosition = startingPosition;
    }

    public FCFS(int startingPosition, boolean isEDFEnabled) {
        this(startingPosition);
        this.isEDFEnabled = isEDFEnabled;
    }

    @Override
    public int start(List<Request> requests, int size) {
        vs = new VectorScatter(isEDFEnabled ? "FCFS EDF" : "FCFS");

        System.out.println("Starting FCFS...");
        if (currentPosition > size || currentPosition < 0) {
            System.out.println("Setting Starting Position to 0...");
            currentPosition = 0;
        }
        int totalMoves = 0;
        if (!isEDFEnabled) {
            for (Request i : requests) {
                totalMoves += Math.abs(currentPosition - i.position);
                currentPosition = i.position;
                i.isCompleted = true;

                vs.addToChart(i);
            }
        } else {
            ArrayList<Request> normalRequests = new ArrayList<>();
            ArrayList<Request> priorityRequests = new ArrayList<>();
            for (Request request : requests) {
                if (request.deadLine == -1) {
                    normalRequests.add(request);
                } else {
                    priorityRequests.add(request);
                }
            }
            System.out.println("EDF Enabled...");
            for (Request normal : normalRequests) {
                Request request = null;
                boolean flag = false;
                for (Request priority : priorityRequests) {
                    if (priority.arrivalTime <= totalMoves) {
                        if (!flag) {
                            request = priority;
                            flag = true;
                        }
                        if (request.deadLine - totalMoves > priority.deadLine - totalMoves) {
                            request = priority;
                        }
                    }
                }
                if (request != null) {
                    totalMoves += Math.abs(currentPosition - request.position);
                    currentPosition = request.position;
                    vs.addToChart(request);
                    if (totalMoves <= request.deadLine) {
                        request.isCompleted = true;
                    }
                    priorityRequests.remove(request);
                }
                totalMoves += Math.abs(currentPosition - normal.position);
                currentPosition = normal.position;
                vs.addToChart(normal);
                normal.isCompleted = true;
            }
            int failed = (int) requests.stream().filter((o) -> !o.isCompleted).count();
            System.out.printf("Failed %s of %s priority requests%n", failed, priorityRequestsAmount);
        }
        return totalMoves;
    }

    @Override
    public void setPriorityRequestsAmount(int amount) {
        priorityRequestsAmount = amount;
    }

    @Override
    public JFreeChart getChart() {
        return vs.createChart();
    }
}
