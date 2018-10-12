package homestation;

import homestation.dialogflow.DialogflowWebhookThread;
import homestation.fitbit.FitbitObject;
import homestation.fitbit.UtilityMethodsFitbit;
import homestation.hospital.HospitalThread;
import homestation.hue.HueObject;
import homestation.mqtt.MQTTPublisher;
import homestation.zway.ZWaySensor;

import java.time.LocalDate;
import java.util.Calendar;

public class MainHomestation {

    public static void main(String[] args) {

        HomestationSettings.createSettings();

        /*LocalDate localDate = LocalDate.now();
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.MINUTE, -5);

        HueObject obj = new HueObject();
        ZWaySensor zway = new ZWaySensor(HomestationSettings.SENSOR_NODE);
        FitbitObject fitbit = UtilityMethodsFitbit.getFitbitAll(HomestationSettings.DTF.format(localDate), HomestationSettings.SDF.format(cal2.getTime()), HomestationSettings.SDF.format(cal.getTime()));

        MQTTPublisher publisher = new MQTTPublisher(obj, zway, fitbit);
        publisher.start();

        DialogflowWebhookThread webhook = new DialogflowWebhookThread(obj, zway);
        webhook.start();

        ControllerThread controller = new ControllerThread(obj, zway, fitbit);
        controller.start();*/

        HospitalThread hospital = new HospitalThread();
        hospital.start();
    }
}