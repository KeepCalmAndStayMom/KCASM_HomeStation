package homestation.hue;

import com.google.gson.Gson;
import java.util.HashMap;

class Hue {

    private int id;
    private boolean on;
    private int bri;
    private int sat;
    private int hue;
    private String effect;
    private String alert;

    Hue(int id) { this.id = id; }

    int getId() {
        return id;
    }

    String toJson() {
        return new Gson().toJson(this).replace("\"id\":"+ String.valueOf(id)+",", "");
    }

    void fromJson(String json) {
        HashMap<String, ?> result = new Gson().fromJson(json, HashMap.class);

        for (String key : result.keySet())
            switch (key) {
                case "on": on = (Boolean) result.get("on"); break;
                case "bri": bri = ((Double) result.get("bri")).intValue(); break;
                case "sat": sat = ((Double) result.get("sat")).intValue(); break;
                case "hue": hue = ((Double) result.get("hue")).intValue(); break;
                case "effect": effect = (String) result.get("effect"); break;
                case "alert": alert = (String) result.get("alert"); break;
            }
    }
}