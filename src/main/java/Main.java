import Algorithms.*;
import Request.RequestBuilder;

import javax.management.InvalidAttributeValueException;

public class Main {
    public static void main(String[] args) throws InvalidAttributeValueException {
        RequestBuilder rb = new RequestBuilder(10, 100);
        DriveSimulation ds = new DriveSimulation(rb);

        Thread thread = new Thread(() -> new XYScatter(ds.getRequests()));
        thread.start();

//        ds.start(new FCFS(0));
//        ds.start(new FCFS(0, true));
//        ds.start(new SSTF(0));
//        ds.start(new SCAN(0, true));
        ds.start(new SCAN(0, true, true));
//        ds.start(new FD_SCAN(0, true));
    }
}
