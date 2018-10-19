package homestation.hospital.graphhopper.geocoding;

import com.google.api.client.util.Key;

public class Hit {
    @Key
    private Point point;

    public Point getPoint() {
        return point;
    }
}