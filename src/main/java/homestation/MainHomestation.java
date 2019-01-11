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

    static MQTTPublisher publisher;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int key = 0;
        HomestationSettings.createSettings();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);

        //HueObject obj = new HueObject();
        //ZWaySensor zway = new ZWaySensor(HomestationSettings.SENSOR_NODE);
        //FitbitObject fitbit = UtilityMethodsFitbit.getFitbitAll(HomestationSettings.DTF.format(LocalDate.now()), HomestationSettings.SDF.format(cal.getTime()), HomestationSettings.SDF.format(Calendar.getInstance().getTime()));

        publisher = new MQTTPublisher(null, null);
        publisher.start();

        DialogflowWebhookThread webhook = new DialogflowWebhookThread(null, null);
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
                    //obj.chromotherapySoft();
                    break;
                case 2:
                    //obj.chromotherapyHard();
                    break;
                case 3:
                    //obj.switchOnHues();
                    break;
                case 4:
                    //obj.switchOffHues();
                    break;
                case 5: sendSensorData();
                    break;
                case 6: sendFitbitData(true);
                    break;
                case 7: sendFitbitData(false);
                    break;
                case 8: HospitalThread.sendMessage(HospitalConstants.GO);
                    break;
                case 9: HospitalThread.sendMessage(HospitalConstants.PREPARE);
                    break;
                case 10:
                    System.exit(0);
                    break;
            }
        }
    }

    private static void sendFitbitData(boolean i) {
            publisher.publishFitbit(i);
    }

    private static void sendSensorData() {
        publisher.publishZWaySensor();
    }

    private static void showMenu() {
        System.out.println("1) Avvia cromo soft");
        System.out.println("2) Avvia cromo hard");
        System.out.println("3) Accendi luci");
        System.out.println("4) Spegni luci");
        System.out.println("5) Invio dati sensore al server");
        System.out.println("6) Invio dati fitbit <90 al server");
        System.out.println("7) Invio dati fitbit >130 al server");
        System.out.println("8) Invio email Controllo Contrazioni Andare");
        System.out.println("9) Invio email Controllo Contrazioni Prepararsi");
        System.out.println("10) Esci");
    }
}