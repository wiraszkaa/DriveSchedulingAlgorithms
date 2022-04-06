package Request;

public class Request implements Cloneable {
    public int id;
    public int position;
    public int arrivalTime;
    public int deadLine;
    public boolean isCompleted;

    public Request(int id, int position, int arrivalTime) {
        this.id = id;
        this.position = position;
        this.arrivalTime = arrivalTime;
        this.isCompleted = false;

        this.deadLine = -1;
    }

    public Request clone() {
        Request clone = new Request(id, position, arrivalTime);
        clone.deadLine = deadLine;
        return clone;
    }
}
