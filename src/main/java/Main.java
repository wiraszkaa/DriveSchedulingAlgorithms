import Algorithms.*;
import Request.RequestBuilder;
import javax.management.InvalidAttributeValueException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InvalidAttributeValueException {
        RequestBuilder rb = new RequestBuilder(1000, 100);

        rb.requestsDensityInSegments(List.of(Amount.LOW, Amount.LOW, Amount.LOW));
        rb.requestsPositionInSegments(List.of(Amount.HIGH, Amount.HIGH, Amount.HIGH));
        rb.requestsDeadlineInSegments(List.of(Amount.HIGH, Amount.MEDIUM, Amount.HIGH));
        rb.requestsPriorityChanceInSegments(List.of(Amount.NONE, Amount.LOW, Amount.NONE));
        DriveSimulation ds = new DriveSimulation(rb);

        ds.start(new FCFS(0));
        ds.start(new FCFS(0, PriorityAlgorithms.EDF));
        ds.start(new FCFS(0, PriorityAlgorithms.FDSCAN));
        ds.start(new SSTF(0));
        ds.start(new SCAN(0, true));
        ds.start(new SCAN(0, true, true));

        ds.showCharts();
    }
}
