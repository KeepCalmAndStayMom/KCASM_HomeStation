package homestation.fitbit;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import homestation.HomestationSettings;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

class FitbitFactory {

    private FitbitUrl url;
    private HttpRequest request;

    Fitbit createSleep(HttpRequestFactory requestFactory, String data) throws IOException {
        if (data == null) data = "today";
        url = new FitbitUrl(HomestationSettings.FITBIT_URL + "sleep/date/" + data + ".json");
        setRequest(requestFactory);
        return request.execute().parseAs(Sleep.class);
    }

    Fitbit createActivities(HttpRequestFactory requestFactory, String data) throws IOException {
        if (data == null) data = "today";
        url = new FitbitUrl(HomestationSettings.FITBIT_URL + "activities/date/" + data + ".json");
        setRequest(requestFactory);
        return request.execute().parseAs(Activities.class);
    }

    Fitbit createHeartRate(HttpRequestFactory requestFactory, String data, String timeStart, String timeEnd) throws IOException {
        if (data == null) data = "today";

        if (timeStart == null || timeEnd == null)
            url = new FitbitUrl(HomestationSettings.FITBIT_URL + "activities/heart/date/" + data + "/1d.json");
        else {
            LocalTime t1 = LocalTime.parse(timeStart);
            t1.minusMinutes(3);
            LocalTime t2 = LocalTime.parse(timeEnd);
            t2.minusMinutes(3);

            url = new FitbitUrl(HomestationSettings.FITBIT_URL + "activities/heart/date/" + data + "/1d/time/" + t1.toString() + "/" + t2.toString() + ".json");
        }
        setRequest(requestFactory);
        Fitbit f = new HeartRate();
        ((HeartRate) f).getData(request);
        return f;
    }

    private void setRequest(HttpRequestFactory requestFactory) throws IOException {
        url.setFields("id, tags, title, url");
        request = requestFactory.buildGetRequest(url);
    }
}