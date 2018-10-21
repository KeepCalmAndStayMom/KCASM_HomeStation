package homestation.hospital;

import com.google.gson.Gson;
import homestation.HomestationSettings;
import homestation.hospital.graphhopper.geocoding.GeocodingResponse;
import homestation.hospital.graphhopper.routing.RoutingResponse;
import smile.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class DistanceEvaluation {
    private static String home = HomestationSettings.HOME_ADDRESS;
    private static String hospital = HomestationSettings.HOSPITAL_ADDRESS;
    private static Gson gson = new Gson();

    static void calculateDistance(Network net) {
        String homeCoordinates = executeRequest("geocoding house", home.replace('-', '+'), null);

        String hospitalCoordinates = executeRequest("geocoding hospital", hospital.replace('-', '+'), null);

        net.setEvidence(HospitalConstants.DISTANCE_NODE, executeRequest("route", homeCoordinates, hospitalCoordinates));
    }

    private static String executeRequest(String type, String param1, String param2) {
        try {
            String urlRequest;

            if (type.equalsIgnoreCase("route"))
                urlRequest = HospitalConstants.ROUTE_API_PARAMS_START + param1 + "&point=" + param2 + HospitalConstants.ROUTE_API_PARAMS_END;
            else
                urlRequest = HospitalConstants.GEO_API_PARAMS_START + param1 + HospitalConstants.GEO_API_PARAMS_END;

            StringBuilder result = new StringBuilder();
            URL url = new URL(urlRequest);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            if (type.equalsIgnoreCase("route")) {
                RoutingResponse route = gson.fromJson(result.toString(), RoutingResponse.class);
                double kilometers = route.getPaths().get(0).getDistance() / 1000;
                int time = route.getPaths().get(0).getTime();
                System.out.println("Distanza in kilometri: " + kilometers + "; tempo in secondi: " + time);
                if (kilometers < HospitalConstants.NEAR)
                    return HospitalConstants.SHORT_DISTANCE;
                else if (kilometers >= HospitalConstants.NEAR && kilometers < HospitalConstants.FAR)
                    return HospitalConstants.MEDIUM_DISTANCE;
                else
                    return HospitalConstants.LONG_DISTANCE;
            }
            else {
                GeocodingResponse geo = gson.fromJson(result.toString(), GeocodingResponse.class);
                double lat = geo.getHits().get(0).getPoint().getLat();
                double lng = geo.getHits().get(0).getPoint().getLng();
                return lat + "," + lng;
            }
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return HospitalConstants.LONG_DISTANCE;
        }
    }
}