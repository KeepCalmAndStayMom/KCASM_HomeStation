package homestation;

import homestation.dialogflow.DialogflowWebhookThread;
import homestation.fitbit.FitbitObject;
import homestation.fitbit.UtilityMethodsFitbit;
import homestation.hospital.*;
import homestation.hue.HueObject;
import homestation.mqtt.MQTTPublisher;
import homestation.zway.ZWaySensor;

import java.time.LocalDate;
import java.util.Calendar;

public class MainHomestation {
    public static void main(String[] args) {
        HomestationSettings.createSettings();

        /*
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);

        HueObject obj = new HueObject();
        ZWaySensor zway = new ZWaySensor(HomestationSettings.SENSOR_NODE);
        FitbitObject fitbit = UtilityMethodsFitbit.getFitbitAll(HomestationSettings.DTF.format(LocalDate.now()), HomestationSettings.SDF.format(cal.getTime()), HomestationSettings.SDF.format(Calendar.getInstance().getTime()));

        MQTTPublisher publisher = new MQTTPublisher(obj, zway, fitbit);
        publisher.start();

        DialogflowWebhookThread webhook = new DialogflowWebhookThread(obj, zway);
        webhook.start();

        ControllerThread controller = new ControllerThread(obj, zway, fitbit);
        controller.start();*/

        HospitalThread hospital = new HospitalThread(new SkipScanStrategy(), new AllFactorsCounterEvaluationStrategy());
        hospital.start();
    }
}