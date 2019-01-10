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
    private int i = 0;

    FitbitThread(MqttTopic topic, FitbitObject fitbit) {
        this.topic = topic;
        this.fitbit = fitbit;
    }

    @Override
    public void run() {
        while (true) {
            i++;

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -1);

            fitbit.updateFitbit(HomestationSettings.DTF.format(LocalDate.now()), HomestationSettings.SDF.format(cal.getTime()), HomestationSettings.SDF.format(Calendar.getInstance().getTime()));

            if (i == 1) {
                try {
                    topic.publish(new MqttMessage(fitbit.toJson().getBytes()));
                } catch (MqttException e) {
                    e.printStackTrace();
                }

                System.out.println("Published message on topic '" + topic.getName() + "': " + fitbit);

                i = 0;
            }

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
