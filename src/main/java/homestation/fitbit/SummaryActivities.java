package homestation.fitbit;

import com.google.api.client.util.Key;
import java.util.ArrayList;

public class SummaryActivities {

    @Key
    int activityCalories;

    @Key
    ArrayList<Distance> distances;

    @Key
    double elevation;

    @Key
    int floors;

    @Key
    int steps;
}