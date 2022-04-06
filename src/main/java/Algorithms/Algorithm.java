package Algorithms;

import Request.Request;
import java.util.List;

public interface Algorithm {
    int start(List<Request> requests, int size);
    void setPriorityRequestsAmount(int amount);
}
