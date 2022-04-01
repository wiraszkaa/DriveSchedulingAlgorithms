package Algorithms;

import Request.Request;
import java.util.List;

public class FCFS implements Algorithm {
    @Override
    public int start(List<Request> requests) {
        System.out.println("Starting FCFS...");
        int totalMoves = 0;
        int currentPosition = 0;
        for(Request i: requests) {
            totalMoves += Math.abs(currentPosition - i.position);
            currentPosition = i.position;
            i.isCompleted = true;
        }
        return totalMoves;
    }
}
