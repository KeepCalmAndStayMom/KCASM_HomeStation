package homestation.mqtt;

import homestation.HomestationSettings;
import homestation.fitbit.FitbitObject;
import homestation.hue.HueObject;
import homestation.zway.ZWaySensor;
import org.eclipse.paho.client.mqttv3.*;


public class MQTTPublisher {

    private static final String TOPIC_HUE = "homestation/" + HomestationSettings.HOMESTATION_ID + "/hue";
    private static final String TOPIC_ZWAY = "homestation/" + HomestationSettings.HOMESTATION_ID + "/zway";
    private static final String TOPIC_FITBIT = "homestation/" + HomestationSettings.HOMESTATION_ID + "/fitbit";

    private HueObject hue;
    private ZWaySensor zway;
    private FitbitObject fitbit;

    private static MqttClient client;

    public MQTTPublisher(HueObject hue, ZWaySensor zway, FitbitObject fitbit) {
        this.hue = hue;
        this.zway = zway;
        this.fitbit = fitbit;

        String clientId = MqttClient.generateClientId();

        try {
            client = new MqttClient(HomestationSettings.BROKER_URL, clientId);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill(client.getTopic("homestation/LWT"), "Il Publisher si è disconnesso".getBytes(), 0, false);
            client.connect(options);

            publishZWaySensor();
            publishFitbit();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void publishHue(String message) {
        MqttTopic hueTopic = client.getTopic(TOPIC_HUE);

        try {
            hueTopic.publish(new MqttMessage(message.getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void publishZWaySensor() {
        MqttTopic zwayTopic = client.getTopic(TOPIC_ZWAY);
        ZWayThread tZway = new ZWayThread(zwayTopic, zway);

        tZway.start();
    }

    private void publishFitbit() {
        MqttTopic fitbitTopic = client.getTopic(TOPIC_FITBIT);
        FitbitThread tFitbit = new FitbitThread(fitbitTopic, fitbit);

        tFitbit.start();
    }
}