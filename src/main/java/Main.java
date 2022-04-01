
import Algorithms.FCFS;
import Algorithms.SSTF;
import Request.RequestBuilder;
import javax.management.InvalidAttributeValueException;

public class Main {
    public static void main(String[] args) throws InvalidAttributeValueException {
        RequestBuilder rb = new RequestBuilder(1000, 100);
        DriveSimulation ds = new DriveSimulation(rb);

        Thread thread = new Thread(() -> new XYScatter(ds.getRequests()));
        thread.start();

        ds.start(new FCFS());
        ds.start(new SSTF());
    }
}
