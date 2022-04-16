package Algorithms;

import Request.Request;

import java.util.List;

public class AlgorithmParameters {
    //FIXED PARAMETERS
    public final int size;
    //PARAMETERS
    public int currentTime;
    public int currentPosition;
    public int totalMoves;
    public int completed;
    public VectorScatter vs;
    //SCAN
    public boolean isMovingRight;
    //C-SCAN
    public boolean isCSCAN;
    public int cSCANSwitches;
    //EDF & FD-SCAN
    public List<Request> priorityRequests;

    public AlgorithmParameters(int size) {
        this.size = size;

        this.isCSCAN = false;
        this.cSCANSwitches = 0;
    }
}
