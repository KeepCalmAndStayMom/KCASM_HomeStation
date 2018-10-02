package homestation.fitbit;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import homestation.HomestationSettings;

import java.io.IOException;
import java.util.ArrayList;

public class UtilityMethodsFitbit {

    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.dir"), "userToken");

    private static FileDataStoreFactory DATA_STORE_FACTORY;

    private static final ArrayList<String> SCOPE =  new ArrayList<String>() {{
        add("activity");
        add("heartrate");
        add("location");
        add("nutrition");
        add("profile");
        add("settings");
        add("sleep");
        add("social");
        add("weight");
    }};

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String TOKEN_SERVER_URL = "https://api.fitbit.com/oauth2/token";
    private static final String AUTHORIZATION_SERVER_URL = "https://www.fitbit.com/oauth2/authorize";

    private static Credential authorize() throws Exception {
        errorIfNotSpecified();

        AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(), HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(TOKEN_SERVER_URL), new BasicAuthentication(HomestationSettings.API_KEY, HomestationSettings.API_SECRET), HomestationSettings.API_KEY, AUTHORIZATION_SERVER_URL).setScopes(SCOPE).setDataStoreFactory(DATA_STORE_FACTORY).build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost(HomestationSettings.CALLBACK_FITBIT).setPort(HomestationSettings.CALLBACK_PORT_FITBIT).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static Fitbit run(HttpRequestFactory requestFactory, TypeDataFitBit type, String data, String timeStart, String timeEnd) throws IOException {
        Fitbit f;
        FitbitFactory factory = new FitbitFactory();

        switch (type) {
            case HEARTRATE: f = factory.createHeartRate(requestFactory, data, timeStart, timeEnd); break;
            case SLEEP: f = factory.createSleep(requestFactory, data); break;
            case ACTIVITIES: f = factory.createActivities(requestFactory, data); break;
            default: f = null;
        }

        return f;
    }

    public synchronized static Fitbit getFitbit(TypeDataFitBit type, String data, String timeStart, String timeEnd){
        try {
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            final Credential credential = authorize();
            HttpRequestFactory requestFactory =
                    HTTP_TRANSPORT.createRequestFactory(request -> {
                        credential.initialize(request);
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    });
            return run(requestFactory, type, data, timeStart, timeEnd);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    public synchronized static FitbitObject getFitbitAll(String data, String timeStart, String timeEnd){
        try {
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            final Credential credential = authorize();
            HttpRequestFactory requestFactory =
                    HTTP_TRANSPORT.createRequestFactory(request -> {
                        credential.initialize(request);
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    });

            return new FitbitObject(run(requestFactory, TypeDataFitBit.ACTIVITIES, data, timeStart, timeEnd),
                    run(requestFactory, TypeDataFitBit.HEARTRATE, data, timeStart, timeEnd),
                    run(requestFactory, TypeDataFitBit.SLEEP, data, timeStart, timeEnd));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    private static void errorIfNotSpecified() {
        if (HomestationSettings.API_KEY.startsWith("Enter ") || HomestationSettings.API_SECRET.startsWith("Enter ")) {
            System.exit(1);
        }
    }
}