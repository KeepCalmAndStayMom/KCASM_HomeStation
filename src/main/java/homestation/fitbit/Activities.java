package homestation.fitbit;

import com.google.api.client.util.Key;

public class Activities extends Fitbit {

    @Key
    SummaryActivities summary;

    @Override
    public synchronized String toJson() {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"activitiesCalories\": ").append(summary.activityCalories).append(",");
        builder.append("\"elevation\": ").append(summary.elevation).append(",");
        builder.append("\"floors\": ").append(summary.floors).append(",");
        builder.append("\"steps\": ").append(summary.steps).append(",");
        builder.append("\"distance\": ").append(getTotalDistance()).append("}");

        return builder.toString();
    }

    @Override
    public Integer getAvgHeartbeats() {
        return null;
    }

    private synchronized Double getTotalDistance(){
        Double distance = 0.0;
        for(Distance d : summary.distances)
            distance+= d.distance;
        return distance;
    }
}