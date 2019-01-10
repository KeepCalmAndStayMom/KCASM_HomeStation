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
import java.util.Scanner;

public class MainHomestation {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int key = 0;
        HomestationSettings.createSettings();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);

        HueObject obj = new HueObject();
        //ZWaySensor zway = new ZWaySensor(HomestationSettings.SENSOR_NODE);
        //FitbitObject fitbit = UtilityMethodsFitbit.getFitbitAll(HomestationSettings.DTF.format(LocalDate.now()), HomestationSettings.SDF.format(cal.getTime()), HomestationSettings.SDF.format(Calendar.getInstance().getTime()));

        MQTTPublisher publisher = new MQTTPublisher(null, null);
        publisher.start();

        DialogflowWebhookThread webhook = new DialogflowWebhookThread(obj, null);
        webhook.start();
        //ControllerThread controller = new ControllerThread(obj, zway, fitbit);
        //controller.start();

        //HospitalThread hospital = new HospitalThread(new SkipScanStrategy(), new AllFactorsCounterEvaluationStrategy());
        //hospital.start();

        while(true) {
            showMenu();
            key = in.nextInt();
            switch(key) {
                case 1:
                    obj.chromotherapySoft();
                    break;
                case 2:
                    obj.chromotherapyHard();
                    break;
                case 3:
                    obj.switchOnHues();
                    break;
                case 4:
                    obj.switchOffHues();
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    System.exit(0);
                    break;
            }
        }
    }

    private static void showMenu() {
        System.out.println("1) Avvia cromo soft");
        System.out.println("2) Avvia cromo hard");
        System.out.println("3) Accendi luci");
        System.out.println("4) Spegni luci");
        System.out.println("5) Invio dati sensore al server");
        System.out.println("6) Invio dati fitbit 75 al server");
        System.out.println("7) Invio dati fitbit 130 al server");
        System.out.println("8) Esci");
    }
}