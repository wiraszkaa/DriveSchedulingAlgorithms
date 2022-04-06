package Algorithms;

import Request.Request;

import java.util.*;

public class FD_SCAN implements Algorithm {
    private int currentPosition;
    private int completed;
    private int totalMoves;
    private int currentTime;
    private boolean isMovingRight;
    ListIterator<Request> iterator;
    int size;

    private int priorityRequestsAmount;
    private final List<Request> priorityRequests;

    public FD_SCAN(int startingPosition, boolean isStartingRight) {
        currentPosition = startingPosition;
        isMovingRight = isStartingRight;

        completed = 0;
        totalMoves = 0;
        currentTime = 0;
        size = 100;
        this.priorityRequests = new ArrayList<>();
    }

    @Override
    public int start(List<Request> requests, int size) {

        // TODO
        System.out.println("Starting FD-SCAN...");
        if (currentPosition > size || currentPosition < 0) {
            System.out.println("Setting Starting Position to 0...");
            currentPosition = 0;
        }
        currentTime = requests.get(0).arrivalTime;
        this.size = size;

        requests.sort(Comparator.comparingInt((o) -> o.position));
        iterator = requests.listIterator();
        for (Request request : requests) {
            if (request.deadLine != -1) {
                priorityRequests.add(request);
            }
        }
        while (completed < requests.size() - priorityRequestsAmount) {
            Request priorityRequest = getPriority(priorityRequests);
            if(priorityRequest != null) {
                move(priorityRequest, priorityRequest.position >= currentPosition);
                scanMovingHandler(priorityRequest, isMovingRight, false);
                priorityRequests.remove(priorityRequest);
            } else {
               move(null, isMovingRight);
            }
        }
        int failed = (int) requests.stream().filter((o) -> !o.isCompleted).count();
        System.out.printf("Failed %s of %s priority requests%n", failed, priorityRequestsAmount);
        return totalMoves;
    }

    private void scanMovingHandler(Request request, boolean isMovingRight, boolean isCount) {
        boolean isAccepted = false;
        if (request.arrivalTime <= currentTime && !request.isCompleted) {
            if (request.position >= currentPosition && isMovingRight) {
                isAccepted = true;
            }
            if (request.position <= currentPosition && !isMovingRight) {
                isAccepted = true;
            }
        }
        if (isAccepted) {
            totalMoves += Math.abs(currentPosition - request.position);
            currentPosition = request.position;
            currentTime = totalMoves;

            request.isCompleted = true;
            if(isCount) {
                completed++;
            }
        }
    }

    private void move(Request lastRequest, boolean isMovingRight) {
        if(isMovingRight) {
            while(iterator.hasNext()) {
                Request request = iterator.next();
                if(request == lastRequest) {
                    break;
                }
                scanMovingHandler(request, true, true);
                if(lastRequest == null && getPriority(priorityRequests) != null) {
                    break;
                }
            }
            if(!iterator.hasNext()) {
                this.isMovingRight = false;
                totalMoves += size - currentPosition;
                currentTime = totalMoves;
                currentPosition = size;
            }
        } else {
            while (iterator.hasPrevious()) {
                Request request = iterator.previous();
                if (request == lastRequest) {
                    break;
                }
                scanMovingHandler(request, false, true);
                if(lastRequest == null && getPriority(priorityRequests) != null) {
                    break;
                }
            }
            if(!iterator.hasPrevious()) {
                this.isMovingRight = true;
                totalMoves += currentPosition;
                currentTime = totalMoves;
                currentPosition = 0;
            }
        }
    }

    private Request getPriority(Collection<Request> priorityRequests) {
        Request priorityRequest = null;
        boolean flag = false;
        boolean found = false;
        for (Request priority : priorityRequests) {
            if(priority.arrivalTime <= currentTime) {
                if (!flag) {
                    priorityRequest = priority;
                    flag = true;
                }
                if (priority.deadLine - totalMoves >= Math.abs(priority.position - currentPosition)) {
                    if (priorityRequest.deadLine - totalMoves > priority.deadLine - totalMoves) {
                        priorityRequest = priority;
                        found = true;
                    }
                }
            }
        }
        return priorityRequest;
    }

    @Override
    public void setPriorityRequestsAmount(int amount) {
        priorityRequestsAmount = amount;
    }
}
