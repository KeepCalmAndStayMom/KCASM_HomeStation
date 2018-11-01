package homestation.hue;

import com.google.gson.Gson;
import homestation.mqtt.MQTTPublisher;
import java.util.ArrayList;
import java.util.HashMap;

public class HueObject {

    private final int NOT_FOUND = -1;
    private final String OPEN_BRACE = "{";
    private final String CLOSED_BRACE = "}";
    private final String COMMA = ", ";
    private ArrayList<Hue> hues = new ArrayList<>();

    public HueObject() {
        for (String s : UtilityMethodsHue.getAllLights().keySet()) {
            hues.add(new Hue(Integer.parseInt(s)));
            HueToObject(Integer.parseInt(s));
        }
    }

    public synchronized void updateAllLights() {
        for(Hue h: hues)
            HueToObject(h.getId());
    }

    public synchronized void switchOnHues() {
        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("ON") + CLOSED_BRACE);
    }

    public synchronized void switchOffHues() {
        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("OFF") + CLOSED_BRACE);
    }

    public synchronized void chromotherapySoft() {
        MQTTPublisher.publishHue("{\"cromoterapia\": \"soft\"}");

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("ON") + COMMA + SettingsHue.SETTINGS.get("BLUE") + COMMA + SettingsHue.SETTINGS.get("MAX_BRI") + COMMA + SettingsHue.SETTINGS.get("MAX_SAT") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("YELLOW") + COMMA + SettingsHue.SETTINGS.get("MID_BRI") + COMMA + SettingsHue.SETTINGS.get("MID_SAT") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("GREEN") + COMMA + SettingsHue.SETTINGS.get("MIN_BRI") + COMMA + SettingsHue.SETTINGS.get("MAX_SAT") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("MAX_BRI") + COMMA + SettingsHue.SETTINGS.get("SELECT") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("MAX_BRI") + COMMA + SettingsHue.SETTINGS.get("NONE_ALERT") + COMMA + SettingsHue.SETTINGS.get("COLORLOOP") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("DEFAULT_COLOR") + COMMA + SettingsHue.SETTINGS.get("NONE_EFFECT") + CLOSED_BRACE);
    }

    public synchronized void chromotherapyHard() {
        MQTTPublisher.publishHue("{\"cromoterapia\": \"hard\"}");

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("ON") + COMMA + SettingsHue.SETTINGS.get("BLUE") + COMMA + SettingsHue.SETTINGS.get("MAX_BRI") + COMMA + SettingsHue.SETTINGS.get("MAX_SAT") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("RED") + COMMA + SettingsHue.SETTINGS.get("MAX_BRI") + COMMA + SettingsHue.SETTINGS.get("MIN_SAT") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("FUCHSIA") + COMMA + SettingsHue.SETTINGS.get("MIN_BRI") + COMMA + SettingsHue.SETTINGS.get("MAX_SAT") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("MAX_BRI") + COMMA + SettingsHue.SETTINGS.get("LSELECT") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("MID_BRI") + COMMA + SettingsHue.SETTINGS.get("NONE_ALERT") + COMMA + SettingsHue.SETTINGS.get("COLORLOOP") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("GREEN") + COMMA + SettingsHue.SETTINGS.get("NONE_EFFECT") + CLOSED_BRACE);
        sleep(5000);

        setAllHues(OPEN_BRACE + SettingsHue.SETTINGS.get("DEFAULT_COLOR") + COMMA + SettingsHue.SETTINGS.get("NONE_EFFECT") + CLOSED_BRACE);
    }

    private void setAllHues(String json) {
        for (Hue h : hues)
            setHue(h, json);
    }

    private boolean checkId(int id) {
        return UtilityMethodsHue.getLight(id + "") != null;
    }

    private int findIndexFromId(int id) {
        if (checkId(id))
            for (int i = 0; i< hues.size(); i++)
                if (hues.get(i).getId() == id)
                    return i;
        return NOT_FOUND;
    }

    private void HueToObject(int id) {
        if (checkId(id)) {
            HashMap result = new Gson().fromJson(UtilityMethodsHue.getLight("" + id), HashMap.class);
            if (findIndexFromId(id) != NOT_FOUND)
                hues.get(findIndexFromId(id)).fromJson(result.get("state").toString());
        }
    }

    private void ObjectToHue(int id) {
        if (findIndexFromId(id) != NOT_FOUND)
            UtilityMethodsHue.huePut("" + id, hues.get(findIndexFromId(id)).toJson());
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void setHue(Hue h, String json){
        h.fromJson(json);
        ObjectToHue(h.getId());
    }
}