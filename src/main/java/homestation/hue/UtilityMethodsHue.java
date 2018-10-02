package homestation.hue;

import com.google.gson.Gson;
import homestation.HomestationSettings;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.HashMap;

class UtilityMethodsHue {

    private static Gson gson = new Gson();

    synchronized static HashMap<String, ?> getAllLights() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(HomestationSettings.API_URL + "/lights");
        HashMap result = new HashMap<>();

        try {
            CloseableHttpResponse response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());
            result = gson.fromJson(json, HashMap.class);
            response.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    synchronized static String getLight(String lightNumber) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(  HomestationSettings.API_URL + "/lights/" + lightNumber);
        String result;

        try {
            CloseableHttpResponse response = client.execute(request);
            result = EntityUtils.toString(response.getEntity());
            response.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    synchronized static String huePut(String lightNumber, String content) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut request = new HttpPut(HomestationSettings.API_URL + "/lights/" + lightNumber + "/state");
        String result = "";

        try {
            request.addHeader("content-type", "application/json");
            request.setEntity(new StringEntity(content));
            CloseableHttpResponse response = client.execute(request);
            result = EntityUtils.toString(response.getEntity());
            response.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}