package Request;

import Algorithms.Amount;
import Request.Request;

import javax.management.InvalidAttributeValueException;
import java.util.*;

public class RequestBuilder {
    private int lastRequestID;
    private int lastRequestArrival;
    private int lastRequestPosition;

    private final int requestsAmount;
    private final int driveSize;
    private int segments;
    private List<Amount> positionInSegments;
    private List<Amount> densityInSegments;
    private final HashMap<Amount, int[]> positionLimits;
    private final HashMap<Amount, int[]> densityLimits;

    private final List<Request> requests;

    public RequestBuilder(int requestsAmount, int driveSize) {
        this.requestsAmount = requestsAmount;
        this.driveSize = driveSize;

        lastRequestID = 0;
        lastRequestArrival = 0;
        lastRequestPosition = 0;

        segments = 3;
        positionInSegments = new ArrayList<>(Arrays.asList(Amount.MEDIUM, Amount.MEDIUM, Amount.MEDIUM));
        densityInSegments = new ArrayList<>(Arrays.asList(Amount.MEDIUM, Amount.MEDIUM, Amount.MEDIUM));
        positionLimits = new HashMap<>();
        positionLimits.put(Amount.LOW, new int[]{5});
        positionLimits.put(Amount.MEDIUM, new int[]{20});
        positionLimits.put(Amount.HIGH, new int[]{100});
        densityLimits = new HashMap<>();
        densityLimits.put(Amount.LOW, new int[]{10, 50});
        densityLimits.put(Amount.MEDIUM, new int[]{3, 10});
        densityLimits.put(Amount.HIGH, new int[]{0, 3});

        requests = new ArrayList<>();
    }

    public boolean segmentsNumber(int amount) {
        if(amount > 0) {
            this.segments = amount;
            return true;
        }
        return false;
    }

    public boolean requestsPositionInSegments(List<Amount> position) {
        if(position.size() == segments) {
            this.positionInSegments = position;
            return true;
        }
        return false;
    }

    public boolean requestsDensityInSegments(List<Amount> requests) {
        if(requests.size() == segments) {
            this.densityInSegments = requests;
            return true;
        }
        return false;
    }

    public boolean positionLimits(Amount amount, int high) {
        if(high >= 0) {
            positionLimits.put(amount, new int[]{high});
            return true;
        }
        return false;
    }

    public boolean densityLimits(Amount amount, int low, int high) {
        if(low < high) {
            densityLimits.put(amount, new int[]{low, high});
            return true;
        }
        return false;
    }

    private void createSegment(int number, Amount BT, Amount density) {
        int minPosition = Math.max(lastRequestPosition - positionLimits.get(BT)[0], 0);
        int maxPosition = Math.min(lastRequestPosition + positionLimits.get(BT)[0], driveSize - 1);
        int lowDensity = densityLimits.get(density)[0];
        int highDensity = densityLimits.get(density)[1];
        for(int i = 0; i < number; i++) {
            Request request = new Request(
                    lastRequestID,
                    rand(minPosition, maxPosition),
                    lastRequestArrival + rand(lowDensity, highDensity));
            requests.add(request);
            lastRequestID++;
            lastRequestArrival = request.arrivalTime;
            lastRequestPosition = request.position;
        }
    }

    public List<Request> create() throws InvalidAttributeValueException {
        System.out.println("Creating " + requestsAmount + " requests...");
        lastRequestID = 0;

        if(positionInSegments.size() != segments || densityInSegments.size() != segments) {
            throw new InvalidAttributeValueException();
        }

        for (int i = 0; i < segments; i++) {
            int number = (int) (requestsAmount * (1.0 / segments));
            if(i == segments - 1) {
                int addition = requestsAmount - lastRequestID - number;
                if(addition != 0) {
                    number += addition;
                }
            }
            createSegment(number, positionInSegments.get(i), densityInSegments.get(i));
        }
        System.out.println(this);
        return requests;
    }

    public List<Request> createCopy() {
        List<Request> copy = new ArrayList<>();
        for(Request i: requests) {
            copy.add(i.clone());
        }
        return copy;
    }

    private int rand(int low, int high) {
        return new Random().nextInt(high - low) + low;
    }

    private String limitsToString(HashMap<Amount, int[]> limits) {
        StringBuilder sb = new StringBuilder("{");
        for(Amount key: limits.keySet()) {
            int[] ints = limits.get(key);
            sb
            .append(key)
            .append("=[");
            for(int i: ints) {
                sb.append(i)
                .append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append("] ");
        }
        sb.setLength(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Parameters:" +
                "\nsegments=" + segments +
                "\npositionInSegments=" + positionInSegments +
                "\ndensityInSegments=" + densityInSegments +
                "\nBTlimits=" + limitsToString(positionLimits) +
                "\ndensityLimits=" + limitsToString(densityLimits);
    }
}
