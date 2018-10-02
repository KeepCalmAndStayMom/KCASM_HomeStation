package homestation.mqtt;

import homestation.hue.HueObject;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class HueThread extends Thread {
    private MqttTopic topic;
    private HueObject hue;

    HueThread(MqttTopic topic, HueObject hue) {
        this.topic = topic;
        this.hue = hue;
    }

    public void run() {
        while (true) {
            hue.updateAllLights();

            try {
                topic.publish(new MqttMessage(hue.toJson().getBytes()));
            } catch (MqttException e) {
                e.printStackTrace();
            }

            System.out.println("Published message on topic '" + topic.getName() + "': " + hue);

            hue.resetCountCromo();

            try {
                Thread.sleep(1800000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
