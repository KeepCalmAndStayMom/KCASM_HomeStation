package homestation.hospital.graphhopper.routing;

import com.google.api.client.util.Key;

import java.util.List;

public class Info {

    @Key
    List<String> copyrights;

    @Key
    int took;

    @Override
    public String toString() {
        return "copyrights: " + copyrights + "; took: " + took;
    }
}