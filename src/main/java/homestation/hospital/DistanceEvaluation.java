package homestation.hospital;

import com.google.gson.Gson;
import homestation.hospital.graphhopper.geocoding.GeocodingResponse;
import homestation.hospital.graphhopper.routing.RoutingResponse;
import smile.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class DistanceEvaluation {

    private static String home;
    private static String hospital;
    private static Gson gson = new Gson();

    static void calculateDistance(Network net) {

        executeRequest("user", null, null);

        home = executeRequest("geocoding house", home, null);

        hospital = executeRequest("geocoding hospital", hospital, null);

        String distance = executeRequest("route", home, hospital);

        //net.setEvidence("Distanza", distance);
    }

    private static String executeRequest(String type, String param1, String param2) {
        try {
            String urlRequest;

            switch (type) {
                case "user":
                    urlRequest = HospitalConstants.USER_URL;
                    break;
                case "route":
                    urlRequest = HospitalConstants.ROUTE_API_PARAMS_START + param1 + "&point=" + param2 + HospitalConstants.ROUTE_API_PARAMS_END;
                    break;
                default:
                    urlRequest = HospitalConstants.GEO_API_PARAMS_START + param1 + HospitalConstants.GEO_API_PARAMS_END;
                    break;
            }

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

            switch (type) {
                case "user":
                    Map<String, Object> map = gson.fromJson(result.toString(), HashMap.class);
                    home = ((String) map.get("home_address")).replace(",", "").replace(' ', '+');
                    hospital = ((String) map.get("hospital_address")).replace(",", "").replace(' ', '+');
                    return "";
                case "route":
                    RoutingResponse route = gson.fromJson(result.toString(), RoutingResponse.class);
                    double distance = route.getPaths().get(0).getDistance();
                    int time = route.getPaths().get(0).getTime();
                    System.out.println("Distanza in metri: " + distance + "; tempo in secondi: " + time);//da ultimare con analisi e trasformazione in kilometri
                    return "";
                default:
                    GeocodingResponse geo = gson.fromJson(result.toString(), GeocodingResponse.class);
                    double lat = geo.getHits().get(0).getPoint().getLat();
                    double lng = geo.getHits().get(0).getPoint().getLng();
                    return lat + "," + lng;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}