package Request;

import Algorithms.Amount;
import javax.management.InvalidAttributeValueException;
import java.util.*;

public class RequestBuilder {
    private int lastRequestID;
    private int lastRequestArrival;
    private int lastRequestPosition;

    private final int requestsAmount;
    private int priorityRequestsAmount;
    private final int driveSize;
    private int segments;
    private List<Amount> positionInSegments;
    private List<Amount> densityInSegments;
    private List<Amount> deadlineInSegments;
    private List<Amount> priorityChanceInSegments;
    private final HashMap<Amount, int[]> positionLimits;
    private final HashMap<Amount, int[]> densityLimits;
    private final HashMap<Amount, int[]> deadlineLimits;
    private final HashMap<Amount, int[]> priorityLimits;

    private final List<Request> requests;

    public RequestBuilder(int requestsAmount, int driveSize) {
        this.requestsAmount = requestsAmount;
        this.driveSize = driveSize;

        lastRequestID = 0;
        lastRequestArrival = 0;
        lastRequestPosition = 0;
        priorityRequestsAmount = 0;

        segments = 3;
        positionInSegments = new ArrayList<>(Arrays.asList(Amount.MEDIUM, Amount.MEDIUM, Amount.MEDIUM));
        densityInSegments = new ArrayList<>(Arrays.asList(Amount.MEDIUM, Amount.MEDIUM, Amount.MEDIUM));
        deadlineInSegments = new ArrayList<>(Arrays.asList(Amount.MEDIUM, Amount.MEDIUM, Amount.MEDIUM));
        priorityChanceInSegments = new ArrayList<>(Arrays.asList(Amount.MEDIUM, Amount.MEDIUM, Amount.MEDIUM));
        positionLimits = new HashMap<>();
        positionLimits.put(Amount.NONE, new int[]{0});
        positionLimits.put(Amount.LOW, new int[]{driveSize / 10});
        positionLimits.put(Amount.MEDIUM, new int[]{driveSize / 5});
        positionLimits.put(Amount.HIGH, new int[]{driveSize});
        densityLimits = new HashMap<>();
        densityLimits.put(Amount.NONE, new int[]{50, 100});
        densityLimits.put(Amount.LOW, new int[]{10, 50});
        densityLimits.put(Amount.MEDIUM, new int[]{3, 10});
        densityLimits.put(Amount.HIGH, new int[]{0, 3});
        deadlineLimits = new HashMap<>();
        deadlineLimits.put(Amount.NONE, new int[]{0, 0});
        deadlineLimits.put(Amount.LOW, new int[]{5, 10});
        deadlineLimits.put(Amount.MEDIUM, new int[]{10, 50});
        deadlineLimits.put(Amount.HIGH, new int[]{50, 100});
        priorityLimits = new HashMap<>();
        priorityLimits.put(Amount.NONE, new int[]{0});
        priorityLimits.put(Amount.LOW, new int[]{10});
        priorityLimits.put(Amount.MEDIUM, new int[]{30});
        priorityLimits.put(Amount.HIGH, new int[]{50});

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

    public boolean requestsDeadlineInSegments(List<Amount> deadline) {
        if(deadline.size() == segments) {
            this.deadlineInSegments = deadline;
            return true;
        }
        return false;
    }

    public boolean requestsPriorityChanceInSegments(List<Amount> priority) {
        if(priority.size() == segments) {
            this.priorityChanceInSegments = priority;
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
        if(low <= high) {
            densityLimits.put(amount, new int[]{low, high});
            return true;
        }
        return false;
    }

    public boolean deadlineLimits(Amount amount, int low, int high) {
        if(low <= high) {
            deadlineLimits.put(amount, new int[]{low, high});
            return true;
        }
        return false;
    }

    public boolean priorityLimits(Amount amount, int high) {
        if(high >= 0 && high <= 100) {
            priorityLimits.put(amount, new int[]{high});
            return true;
        }
        return false;
    }

    private void createSegment(int number, Amount BT, Amount density, Amount deadline, Amount priorityChance) {
        int minPosition = Math.max(lastRequestPosition - positionLimits.get(BT)[0], 0);
        int maxPosition = Math.min(lastRequestPosition + positionLimits.get(BT)[0], driveSize - 1);
        int lowDensity = densityLimits.get(density)[0];
        int highDensity = densityLimits.get(density)[1];
        int minDeadline = deadlineLimits.get(deadline)[0];
        int maxDeadline = deadlineLimits.get(deadline)[1];
        int chance = priorityLimits.get(priorityChance)[0];

        for(int i = 0; i < number; i++) {
            int randomPosition = maxPosition;
            if(minPosition < maxPosition) {
                randomPosition = rand(minPosition, maxPosition);
            }

            Request request = new Request(
                    lastRequestID,
                    randomPosition,
                    lastRequestArrival + rand(lowDensity, highDensity));

            int randomDeadline = request.arrivalTime + maxDeadline;
            if(minDeadline < maxDeadline) {
                randomDeadline = rand(request.arrivalTime + minDeadline, request.arrivalTime + maxDeadline);
            }

            if(rand(0, 100) < chance) {
                request.deadLine = randomDeadline;
                priorityRequestsAmount++;
            }

            requests.add(request);
            lastRequestID++;
            lastRequestArrival = request.arrivalTime;
            lastRequestPosition = request.position;
        }
    }

    public List<Request> create() throws InvalidAttributeValueException {
        System.out.println("Creating " + requestsAmount + " requests...");
        lastRequestID = 0;

        if(positionInSegments.size() != segments || densityInSegments.size() != segments ||
                deadlineInSegments.size() != segments || priorityChanceInSegments.size() != segments) {
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
            createSegment(number,
                    positionInSegments.get(i),
                    densityInSegments.get(i),
                    deadlineInSegments.get(i),
                    priorityChanceInSegments.get(i));
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
                "\ndeadlineInSegments=" + deadlineInSegments +
                "\npriorityChanceInSegments=" + priorityChanceInSegments +
                "\npositionlimits=" + limitsToString(positionLimits) +
                "\ndensityLimits=" + limitsToString(densityLimits) +
                "\ndeadlineLimits=" + limitsToString(deadlineLimits) +
                "\npriorityLimits=" + limitsToString(priorityLimits);
    }

    public int getDriveSize() {
        return driveSize;
    }

    public int getPriorityRequestsAmount() {
        return priorityRequestsAmount;
    }
}
