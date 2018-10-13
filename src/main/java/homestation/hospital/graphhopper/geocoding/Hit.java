package homestation.hospital.graphhopper.geocoding;

import com.google.api.client.util.Key;

import java.util.List;

public class Hit {

    @Key
    int osm_id;

    @Key
    String osm_type;

    @Key
    String country;

    @Key
    String osm_value;

    @Key
    String postcode;

    @Key
    String name;

    @Key
    String state;

    @Key
    Point point;

    @Key
    List<Double> extent;

    public Point getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "OSM ID: " + osm_id + "; OSM type: " + osm_type + "; OSM value: " + osm_value + "; postal code: " + postcode + "; name: " + name + "; state: " + state + "; country: " + country + "; coordinates: [" + point + "]; extent: " + extent;
    }
}