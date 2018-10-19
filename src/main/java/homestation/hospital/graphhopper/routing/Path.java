package homestation.hospital.graphhopper.routing;

import com.google.api.client.util.Key;

public class Path {
    @Key
    private double distance;

    @Key
    private int time;

    public double getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }
}