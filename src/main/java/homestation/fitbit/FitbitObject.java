package homestation.fitbit;

public class FitbitObject {

    private Activities activities;
    public HeartRate heartRate;
    private Sleep sleep;

    FitbitObject(Fitbit activities, Fitbit heartRate, Fitbit sleep) {
        this.activities = (Activities) activities;
        this.heartRate = (HeartRate) heartRate;
        this.sleep = (Sleep) sleep;
    }

    public synchronized void updateFitbit(String data, String timeStart, String timeEnd) {
        FitbitObject fo = UtilityMethodsFitbit.getFitbitAll(data, timeStart, timeEnd);
        if (fo!=null) {
            activities = fo.activities;
            heartRate = fo.heartRate;
            sleep = fo.sleep;
        }
    }

    public synchronized String toJson() {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"Activities\": ").append(activities.toJson()).append(",");
        builder.append("\"Sleep\": ").append(sleep.toJson()).append(",");
        builder.append("\"HeartRate\": ").append(heartRate.toJson()).append("}");

        return builder.toString();
    }
}
