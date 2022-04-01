
import Algorithms.Algorithm;
import Request.Request;
import Request.RequestBuilder;

import javax.management.InvalidAttributeValueException;
import java.util.Collection;

public class DriveSimulation {
    private final RequestBuilder requestBuilder;
    private final Collection<Request> requests;

    public DriveSimulation(RequestBuilder requestBuilder) throws InvalidAttributeValueException {
        this.requestBuilder = requestBuilder;
        this.requests = requestBuilder.create();
    }

    public void start(Algorithm algorithm) {
        long start = System.nanoTime();
        int totalMoves = algorithm.start(requestBuilder.createCopy());
        long finish = (System.nanoTime() - start);
        System.out.println(" moved by " + totalMoves);
        System.out.println("Simulation took " + ElapsedTimeString.getTime(finish));
    }

    public Collection<Request> getRequests() {
        return requests;
    }
}

