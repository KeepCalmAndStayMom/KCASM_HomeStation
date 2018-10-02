package homestation.mqtt;

import homestation.HomestationSettings;
import homestation.fitbit.FitbitObject;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import java.time.LocalDate;
import java.util.Calendar;

public class FitbitThread extends Thread {
    private MqttTopic topic;
    private FitbitObject fitbit;

    FitbitThread(MqttTopic topic, FitbitObject fitbit) {
        this.topic = topic;
        this.fitbit = fitbit;
    }

    @Override
    public void run() {
        while (true) {

            LocalDate localDate = LocalDate.now();
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal2.add(Calendar.MINUTE, -1);

            fitbit.updateFitbit(HomestationSettings.DTF.format(localDate), HomestationSettings.SDF.format(cal2.getTime()), HomestationSettings.SDF.format(cal.getTime()));

            try {
                topic.publish(new MqttMessage(fitbit.toJson().getBytes()));
            } catch (MqttException e) {
                e.printStackTrace();
            }

            System.out.println("Published message on topic '" + topic.getName() + "': " + fitbit);

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
