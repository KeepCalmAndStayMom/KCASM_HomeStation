package homestation.hospital.graphhopper.routing;

import com.google.api.client.util.Key;

public class Path {

    @Key
    double distance;

    @Key
    double weight;

    @Key
    int time;

    @Key
    int transfers;

    @Key
    String snapped_waypoints;

    public double getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "distance: " + distance + "; time: " + time + "; weight: " + weight + "; transfers: " + transfers + "; snapped waypoints: " + snapped_waypoints;
    }
}