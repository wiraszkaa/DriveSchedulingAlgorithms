package Algorithms;

import Request.Request;
import java.util.Collection;
import java.util.List;

public class RequestAdder {
    public static int addRequests(List<Request> requests, Collection<Request> currentRequests, int lastRequest, double currentTime) {
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
