package homestation.hospital.graphhopper.geocoding;

import com.google.api.client.util.Key;

public class Point {
    @Key
    double lat;

    @Key
    double lng;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return "latitude: " + lat + "; longitude: " + lng;
    }
}