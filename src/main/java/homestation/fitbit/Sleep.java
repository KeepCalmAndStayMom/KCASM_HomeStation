package homestation.fitbit;

import com.google.api.client.util.Key;

public class Sleep extends Fitbit {

    @Key
    int minutesAsleep;

    @Key
    int minutesAwake;

    @Override
    public synchronized String toJson() {
       StringBuilder builder = new StringBuilder();

       builder.append("{\"minutesAsleep\": ").append(minutesAsleep).append(",");
       builder.append("\"minutesAwake\": ").append(minutesAwake).append("}");

       return builder.toString();
    }

    @Override
    public Integer getAvgHeartbeats() {
        return null;
    }
}