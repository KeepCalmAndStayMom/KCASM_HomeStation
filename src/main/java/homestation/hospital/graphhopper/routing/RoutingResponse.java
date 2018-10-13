package homestation.hospital.graphhopper.routing;

import com.google.api.client.util.Key;

import java.util.List;

public class RoutingResponse {
    @Key
    Info info;

    @Key
    List<Path> paths;

    public List<Path> getPaths() {
        return paths;
    }

    @Override
    public String toString() {
        return "info: " + info + "; paths: " + paths;
    }
}