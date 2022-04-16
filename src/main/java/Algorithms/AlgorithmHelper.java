package Algorithms;

import Request.Request;
import java.util.List;
import java.util.ListIterator;

public class AlgorithmHelper {

    public static int handleEDF(AlgorithmParameters ap) {
        Request request;
        int failedCount = 0;
        do {
            request = null;
            boolean flag = false;
            for (Request priority : ap.priorityRequests) {
                if (priority.arrivalTime <= ap.currentTime) {
                    if (!flag) {
                        request = priority;
                        flag = true;
                    }
                    if (request.deadLine > priority.deadLine) {
                        request = priority;
                    }
                }
            }
            if (request != null) {
                int movedBy = Math.abs(ap.currentPosition - request.position);
                int deadlineTime = Math.max(request.deadLine - ap.currentTime, 0);
                if (movedBy <= deadlineTime) {
                    ap.currentPosition = request.position;
                    request.isCompleted = true;
                } else {
                    movedBy = deadlineTime;
                    if (request.position >= ap.currentPosition) {
                        ap.currentPosition += movedBy;
                    } else {
                        ap.currentPosition -= movedBy;
                    }
                    failedCount++;
                }
                ap.totalMoves += movedBy;
                ap.currentTime += movedBy;
                ap.vs.addToChart(request);
                ap.priorityRequests.remove(request);
            }
        } while (request != null);

        return failedCount;
    }

    public static void handleFDSCAN(List<Request> normalRequests, AlgorithmParameters ap) {
        Request request;
        ListIterator<Request> iterator = normalRequests.listIterator();
        do {
            request = getPriority(ap);
            if(request != null) {
                if (ap.currentPosition >= request.position) {
                    while(iterator.hasNext()) {
                        if (iterator.next().position >= ap.currentPosition) {
                            break;
                        }
                    }
                    if(iterator.hasPrevious()) {
                        iterator.previous();
                    }
                    ap.isMovingRight = true;
                    moveRight(request, iterator, ap);
                } else {
                    while(iterator.hasNext()) {
                        if(iterator.next().position > ap.currentPosition) {
                            break;
                        }
                    }
                    ap.isMovingRight = false;
                    moveLeft(request, iterator, ap);
                }
                scanMovingHandler(request, ap);
            }
        } while (request != null);
    }

    public static Request getPriority(AlgorithmParameters ap) {
        Request priorityRequest = null;
        boolean flag = false;
        boolean found = false;
        for (Request priority : ap.priorityRequests) {
            if(priority.arrivalTime <= ap.currentTime) {
                if (!flag) {
                    priorityRequest = priority;
                    flag = true;
                }
                if (priority.deadLine - ap.currentTime >= Math.abs(priority.position - ap.currentPosition)) {
                    if (priorityRequest.deadLine > priority.deadLine) {
                        priorityRequest = priority;
                    }
                    found = true;
                }
            }
        }
        if (found) {
            ap.priorityRequests.remove(priorityRequest);
            return priorityRequest;
        } else {
            return null;
        }
    }

    public static void move(Request lastRequest, ListIterator<Request> iterator, AlgorithmParameters ap) {
        if (ap.isMovingRight) {
            moveRight(lastRequest, iterator, ap);
        } else {
            moveLeft(lastRequest, iterator, ap);
        }
    }

    public static void moveLeft(Request lastRequest, ListIterator<Request> iterator, AlgorithmParameters ap) {
        while (iterator.hasPrevious()) {
            Request request = iterator.previous();
            if (request == lastRequest) {
                break;
            }
            scanMovingHandler(request, ap);
        }
        if (!iterator.hasPrevious()) {
            ap.totalMoves += ap.currentPosition;
            ap.currentTime += ap.currentPosition;
            if (!ap.isCSCAN) {
                ap.isMovingRight = true;
                ap.currentPosition = 0;
            } else {
                while(iterator.hasNext()) {
                    iterator.next();
                }
                ap.currentPosition = ap.size;
                ap.cSCANSwitches++;
            }
        }
    }

    public static void moveRight(Request lastRequest, ListIterator<Request> iterator, AlgorithmParameters ap) {
        while (iterator.hasNext()) {
            Request request = iterator.next();
            if (request == lastRequest) {
                break;
            }
            scanMovingHandler(request, ap);
        }
        if (!iterator.hasNext()) {
            int movedBy = ap.size - ap.currentPosition;
            ap.totalMoves += movedBy;
            ap.currentTime += movedBy;
            if (!ap.isCSCAN) {
                ap.isMovingRight = false;
                ap.currentPosition = ap.size;
            } else {
                while(iterator.hasPrevious()) {
                    iterator.previous();
                }
                ap.currentPosition = 0;
                ap.cSCANSwitches++;
            }
        }
    }

    public static void scanMovingHandler(Request request, AlgorithmParameters ap) {
        boolean isAccepted = false;
        int movedBy = Math.abs(ap.currentPosition - request.position);
        if (request.arrivalTime <= (ap.currentTime + movedBy) && !request.isCompleted) {
            if (request.position >= ap.currentPosition && ap.isMovingRight) {
                isAccepted = true;
            }
            if (request.position <= ap.currentPosition && !ap.isMovingRight) {
                isAccepted = true;
            }
        }
        if (isAccepted) {
            handleRequest(request, movedBy, ap);
        }
    }

    public static void handleRequest(Request request, int movedBy, AlgorithmParameters ap) {
        ap.currentTime += movedBy;
        ap.totalMoves += movedBy;
        ap.currentPosition = request.position;

        request.isCompleted = true;
        ap.vs.addToChart(request);
        ap.completed++;
    }
}
