package homestation.mqtt;

import homestation.zway.ZWaySensor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class ZWayThread extends Thread {

    private MqttTopic topic;
    private ZWaySensor zway;

    ZWayThread(MqttTopic topic, ZWaySensor zway) {
        this.topic = topic;
        this.zway = zway;
    }

    public void run() {
        while (true) {
            zway.sensorsManagement();

            try {
                topic.publish(new MqttMessage(zway.toJson().getBytes()));
            } catch (MqttException e) {
                e.printStackTrace();
            }

            System.out.println("Published message on topic '" + topic.getName() + "': " + zway);

            try {
                Thread.sleep(7200000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
