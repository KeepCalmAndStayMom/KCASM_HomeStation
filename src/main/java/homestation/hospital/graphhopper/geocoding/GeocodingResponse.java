package homestation.hospital.graphhopper.geocoding;

import com.google.api.client.util.Key;
import java.util.List;

public class GeocodingResponse {
    @Key
    private List<Hit> hits;

    public List<Hit> getHits() {
        return hits;
    }
}