package homestation.mqtt;

import homestation.HomestationSettings;
import homestation.fitbit.FitbitObject;
import homestation.hue.HueObject;
import homestation.zway.ZWaySensor;
import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MQTTPublisher {

    private static final String TOPIC_HUE = "homestation/" + HomestationSettings.HOMESTATION_ID + "/hue";
    private static final String TOPIC_ZWAY = "homestation/" + HomestationSettings.HOMESTATION_ID + "/zway";
    private static final String TOPIC_FITBIT = "homestation/" + HomestationSettings.HOMESTATION_ID + "/fitbit";

    private ZWaySensor zway;
    private FitbitObject fitbit;

    private static MqttClient client;

    public MQTTPublisher(ZWaySensor zway, FitbitObject fitbit) {
        this.zway = zway;
        this.fitbit = fitbit;

        String clientId = MqttClient.generateClientId();

        try {
            client = new MqttClient(HomestationSettings.BROKER_URL, clientId);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    List<String> zway_list_messages;
    List<String> fitbit_list_messages;

    public void start() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill(client.getTopic("homestation/LWT"), "Il Publisher si è disconnesso".getBytes(), 0, false);
            client.connect(options);

            //publishZWaySensor();
            //publishFitbit();

            zway_list_messages = new ArrayList<>();

            zway_list_messages.add(zwaytoJson(false,15,10,60));
            zway_list_messages.add(zwaytoJson(false,20,100,50));
            zway_list_messages.add(zwaytoJson(true,25,300,40));
            zway_list_messages.add(zwaytoJson(true,30,550,30));
            zway_list_messages.add(zwaytoJson(false,35,800,20));


            fitbit_list_messages = new ArrayList<>();

            fitbit_list_messages.add(fitbittoJson(700,0.5,1,250,0.5,240,60,60));
            fitbit_list_messages.add(fitbittoJson(1000,0.5,1,400,0.7,130,55,70));
            fitbit_list_messages.add(fitbittoJson(359,1,1,600,1.2,130,130,80));
            fitbit_list_messages.add(fitbittoJson(2000,2,3,1000,2,23,240,65));
            fitbit_list_messages.add(fitbittoJson(3500,3.4,4,1700,3.4,345,135,75));
            fitbit_list_messages.add(fitbittoJson(156,5.3,3,800,1.6,123,45,130));
            fitbit_list_messages.add(fitbittoJson(1245,6.8,5,2800,5.6,600,12,120));
            fitbit_list_messages.add(fitbittoJson(2301,5.4,5,1650,3.3,30,250,140));
            fitbit_list_messages.add(fitbittoJson(1005,0.9,2,100,0.2,15,300,180));
            fitbit_list_messages.add(fitbittoJson(870,1,1,500,0.9,240,56,160));

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private String zwaytoJson(boolean movement, double temperature, double luminescence, double humidity) {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"movement\": ").append(movement).append(",");
        builder.append("\"temperature\": ").append(temperature).append(",");
        builder.append("\"luminescence\": ").append(luminescence).append(",");
        builder.append("\"humidity\": ").append(humidity).append("}");

        return builder.toString();
    }


    private String fitbittoJson(int calories, double elevation, int floors, int steps, double distance, int minutesAsleep, int minutesAwake, int heartbeats) {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"Activities\": ").append("{\"activitiesCalories\": ").append(calories).append(",");
        builder.append("\"elevation\": ").append(elevation).append(",");
        builder.append("\"floors\": ").append(floors).append(",");
        builder.append("\"steps\": ").append(steps).append(",");
        builder.append("\"distance\": ").append(distance).append("}").append(",");

        builder.append("\"Sleep\": ").append("{\"minutesAsleep\": ").append(minutesAsleep).append(",");
        builder.append("\"minutesAwake\": ").append(minutesAwake).append("}").append(",");
        builder.append("\"HeartRate\": ").append("{\"heartbeats\": ").append(heartbeats).append("}").append("}");

        return builder.toString();
    }


    public static void publishHue(String message) {
        MqttTopic hueTopic = client.getTopic(TOPIC_HUE);

        try {
            hueTopic.publish(new MqttMessage(message.getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishZWaySensor() {
        MqttTopic zwayTopic = client.getTopic(TOPIC_ZWAY);
        //ZWayThread tZway = new ZWayThread(zwayTopic, zway);
        //tZway.start();

        Random rand = new Random();
        int i = rand.nextInt(5);
        System.out.println("ZWAY:  " + zway_list_messages.get(i));
        try {
            zwayTopic.publish(new MqttMessage(zway_list_messages.get(i).getBytes()));

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void publishFitbit(boolean bool) {
        MqttTopic fitbitTopic = client.getTopic(TOPIC_FITBIT);
        //FitbitThread tFitbit = new FitbitThread(fitbitTopic, fitbit);
        //tFitbit.start();

        Random rand = new Random();
        int i;
        if(bool)
            i = rand.nextInt(5);
        else
            i = rand.nextInt(5) + 5;
        System.out.println("FITBIT:  " + fitbit_list_messages.get(i));

        try {
            fitbitTopic.publish(new MqttMessage(fitbit_list_messages.get(i).getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}