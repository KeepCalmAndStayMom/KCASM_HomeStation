package homestation.fitbit;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.ArrayMap;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HeartRate extends Fitbit {

    private ArrayList<SamplingHeartbeat> heartbeats;

    void getData(HttpRequest request) throws IOException {
        ArrayList<ArrayMap> dataset = (ArrayList) ((ArrayMap) request.execute().parseAs(HashMap.class).get("activities-heart-intraday")).get("dataset");

        heartbeats = new ArrayList<>();

        for (ArrayMap data : dataset)
            heartbeats.add(new SamplingHeartbeat(((BigDecimal) data.get("value")).intValueExact(), ((String) data.get("time"))));

        Collections.sort(heartbeats);
    }

    @Override
    public synchronized String toJson() {
       StringBuilder builder = new StringBuilder();

       builder.append("{\"heartbeats\": ").append(getAvgHeartbeats()).append("}");

       return builder.toString();
    }

    @Override
    public synchronized Integer getAvgHeartbeats() {
        if (heartbeats.isEmpty())
            return null;
        int avg = 0, dim = 0;
        for (SamplingHeartbeat i : heartbeats)
            if (i.heartbeat != null) {
                avg += i.heartbeat;
                dim++;
            }

        if(dim == 0)
            return null;

        return avg/dim;
    }

    public ArrayList<SamplingHeartbeat> getHeartbeats() {
        return heartbeats;
    }
}