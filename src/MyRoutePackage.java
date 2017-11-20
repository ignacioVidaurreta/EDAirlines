import java.util.LinkedList;

/**
 * Created by Bensas on 11/20/17.
 */
public class MyRoutePackage {
    double best;
    double totalTime;
    LinkedList<MyFlightPackage> route;

    public MyRoutePackage(double best, double totalTime, LinkedList<MyFlightPackage> route) {
        this.best = best;
        this.totalTime = totalTime;
        this.route = route;
    }
}
