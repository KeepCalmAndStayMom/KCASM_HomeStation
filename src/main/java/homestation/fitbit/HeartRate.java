package homestation.fitbit;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.ArrayMap;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class HeartRate extends Fitbit {

    private ArrayList<SamplingHeartbeat> heartbeats;

    void getData(HttpRequest request) throws IOException {
        ArrayList<ArrayMap> dataset = (ArrayList) ((ArrayMap) request.execute().parseAs(HashMap.class).get("activities-heart-intraday")).get("dataset");

        heartbeats = new ArrayList<>();

        for (ArrayMap data : dataset)
            heartbeats.add(new SamplingHeartbeat(((BigDecimal) data.get("value")).intValueExact(), ((String) data.get("time"))));

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
        int avg = 0;
        for (SamplingHeartbeat i : heartbeats)
            avg += i.heartbeat;
        return avg/heartbeats.size();
    }
}