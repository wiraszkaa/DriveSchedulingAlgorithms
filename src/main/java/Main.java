import Algorithms.*;
import Request.RequestBuilder;
import javax.management.InvalidAttributeValueException;

public class Main {
    public static void main(String[] args) throws InvalidAttributeValueException {
        RequestBuilder rb = new RequestBuilder(100, 100);
        DriveSimulation ds = new DriveSimulation(rb);

        ds.start(new FCFS(0));
//        ds.start(new FCFS(0, true));
        ds.start(new SSTF(0));
        ds.start(new SCAN(0, true));
        ds.start(new SCAN(0, true, true));
//        ds.start(new FD_SCAN(0, true));

        ds.showCharts();
    }
}
