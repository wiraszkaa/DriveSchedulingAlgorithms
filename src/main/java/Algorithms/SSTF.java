package Algorithms;

import Request.Request;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SSTF implements Algorithm {
    @Override
    public int start(List<Request> requests) {
        System.out.println("Starting SSTF...");
        int lastRequest = 0;

        int completed = 0;
        int totalMoves = 0;

        int currentTime = requests.get(0).arrivalTime;
        int currentPosition = 0;
        List<Request> currentRequests = new ArrayList<>();

        while(completed < requests.size()) {
            lastRequest = addRequests(requests, currentRequests, lastRequest, currentTime);
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
                completed++;
            } else {
                if(lastRequest < requests.size()) {
                    currentTime = requests.get(lastRequest).arrivalTime;
                }
            }
        }
        return totalMoves;
    }

    private static int addRequests(List<Request> requests, Collection<Request> currentRequests, int lastRequest, double currentTime) {
        for(int i = lastRequest; i < requests.size(); i++) {
            Request request = requests.get(i);
            if(request.arrivalTime <= currentTime) {
                currentRequests.add(request);
                lastRequest = request.id + 1;
            } else {
                break;
            }
        }
        return lastRequest;
    }
}
