package homestation.hospital.graphhopper.routing;

import com.google.api.client.util.Key;
import java.util.List;

public class RoutingResponse {
    @Key
    private List<Path> paths;

    public List<Path> getPaths() {
        return paths;
    }
}