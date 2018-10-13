package homestation.hospital.graphhopper.geocoding;

import java.util.List;

public class GeocodingResponse {
    List<Hit> hits;
    int took;

    public List<Hit> getHits() {
        return hits;
    }

    @Override
    public String toString() {
        return hits + "; took: " + took;
    }
}