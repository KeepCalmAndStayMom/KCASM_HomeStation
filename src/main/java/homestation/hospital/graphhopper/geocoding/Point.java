package homestation.hospital.graphhopper.geocoding;

import com.google.api.client.util.Key;

public class Point {
    @Key
    private double lat;

    @Key
    private double lng;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}