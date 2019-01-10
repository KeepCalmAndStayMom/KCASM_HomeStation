package homestation.hue;

import java.util.HashMap;

public class SettingsHue {

    public final static HashMap<String, String> SETTINGS = new HashMap<>();
    static {
        SETTINGS.put("ON", "\"on\": true");
        SETTINGS.put("OFF", "\"on\": false");

        SETTINGS.put("MIN_BRI", "\"bri\": 50");
        SETTINGS.put("MID_BRI", "\"bri\": 140");
        SETTINGS.put("MAX_BRI", "\"bri\": 254");

        SETTINGS.put("MIN_SAT", "\"sat\": 50");
        SETTINGS.put("MID_SAT", "\"sat\": 140");
        SETTINGS.put("MAX_SAT", "\"sat\": 254");

        SETTINGS.put("RED", "\"hue\": 0");
        SETTINGS.put("YELLOW", "\"hue\": 12750");
        SETTINGS.put("GREEN", "\"hue\": 25500");
        SETTINGS.put("BLUE", "\"hue\": 46920");
        SETTINGS.put("FUCHSIA", "\"hue\": 56100");
        SETTINGS.put("DEFAULT_COLOR", "\"hue\": 8418");

        SETTINGS.put("NONE_EFFECT", "\"effect\": \"none\"");
        SETTINGS.put("COLORLOOP", "\"effect\": \"colorloop\"");

        SETTINGS.put("NONE_ALERT", "\"alert\": \"none\"");
        SETTINGS.put("SELECT", "\"alert\": \"select\"");
        SETTINGS.put("LSELECT", "\"alert\": \"lselect\"");
    }
}
