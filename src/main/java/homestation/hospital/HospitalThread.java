package homestation.hospital;

import homestation.HomestationSettings;
import homestation.fitbit.SamplingHeartbeat;
import smile.Network;

import java.time.LocalDate;
import java.util.*;

public class HospitalThread extends Thread {

    private LocalDate pregnancyStart = HomestationSettings.PREGNANCY_START_DATE;

    private boolean emailNotification = HomestationSettings.EMAIL_NOTIFICATION;
    private boolean SMSNotification = HomestationSettings.SMS_NOTIFICATION;

    private SamplingListScanStrategy scanStrategy;
    private SamplingListEvaluationStrategy evaluationStrategy;

    public HospitalThread(SamplingListScanStrategy scanStrategy, SamplingListEvaluationStrategy evaluationStrategy) {
        this.scanStrategy = scanStrategy;
        this.evaluationStrategy = evaluationStrategy;
    }

    public void setStrategies(SamplingListScanStrategy scanStrategy, SamplingListEvaluationStrategy evaluationStrategy) {
        this.scanStrategy = scanStrategy;
        this.evaluationStrategy = evaluationStrategy;
    }

    @Override
    public void run() {
        System.setProperty("jsmile.native.library", "C:/Users/Paolo/IdeaProjects/KCASM_HomeStation/lib/jsmile.dll");
        new smile.License(
                "SMILE LICENSE f08b4722 ab3866b5 ead85dce " +
                        "THIS IS AN ACADEMIC LICENSE AND CAN BE USED " +
                        "SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " +
                        "AS DEFINED IN THE BAYESFUSION ACADEMIC " +
                        "SOFTWARE LICENSING AGREEMENT. " +
                        "Serial #: 4hv2w9fklobw242k1zo4ri97i " +
                        "Issued for: Paolo Lamberto (paul.lamberto@hotmail.it) " +
                        "Academic institution: Universit\u00e0 del Piemonte Orientale - UPO " +
                        "Valid until: 2019-02-23 " +
                        "Issued by BayesFusion activation server",
                new byte[] {
                        123,96,49,52,126,90,-85,-114,121,-17,114,75,117,-124,-25,73,
                        19,117,-74,-115,-84,121,-121,36,23,14,99,-43,3,119,-11,114,
                        34,83,-108,-23,-4,104,-112,82,119,-83,-18,-13,121,-120,26,28,
                        49,-70,-29,5,-122,-42,-75,103,-116,-54,-3,-113,-88,-25,54,61
                }
        );

        Network net = new Network();
        net.readFile("src/main/resources/rete_parto.xdsl");

        //calcolo la distanza: uso la API di geocoding per ottenere le coordinate, poi uso la API di routing passando le coordinate per ottenere la distanza
        DistanceEvaluation.calculateDistance(net);

        while (true) {
            //chiedo a Fitbit i dati dell'ultima mezz'ora (qui ci sono liste già pronte per i test ma nell'applicazione reale uso la API di Fitbit)
            ArrayList<SamplingHeartbeat> l = new ArrayList<>();
            Random rand = new Random();
            int randomTest = rand.nextInt(8) + 1;
            switch (randomTest) {
                case 1:
                    CreateSamplingHeartbeatTestList.createList1(l);
                    break;
                case 2:
                    CreateSamplingHeartbeatTestList.createList2(l);
                    break;
                case 3:
                    CreateSamplingHeartbeatTestList.createList3(l);
                    break;
                case 4:
                    CreateSamplingHeartbeatTestList.createList4(l);
                    break;
                case 5:
                    CreateSamplingHeartbeatTestList.createList5(l);
                    break;
                case 6:
                    CreateSamplingHeartbeatTestList.createTestList1(l);
                    break;
                case 7:
                    CreateSamplingHeartbeatTestList.createTestList2(l);
                    break;
                default:
                    CreateSamplingHeartbeatTestList.createRealisticList(l);
                    break;
            }
            ContractionEvaluation.calculateContraction(l, net, pregnancyStart, scanStrategy, evaluationStrategy);

            //calcolo la EU di andare e non andare in ospedale, se è meglio andare o prepararsi viene mandata una notifica
            net.updateBeliefs();

            printValues(net);

            sendNotifications(net);

            try {
                Thread.sleep(HospitalConstants.CONTRACTION_EVALUATION_FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //metodo temporaneo di test che stampa tutte le probabilità degli outcome
    private void printValues(Network net) {
        double[] contrValues = net.getNodeValue(HospitalConstants.CONTRACTION_NODE);
        System.out.println("__________________\nContrazione");
        for (int i = 0; i < contrValues.length; i ++) {
            System.out.println(net.getOutcomeId(HospitalConstants.CONTRACTION_NODE, i) + " = " + (int) (contrValues[i] * 100) + "%");
        }

        double[] weekValues = net.getNodeValue(HospitalConstants.PREGNANCY_WEEK_NODE);
        System.out.println("__________________\nSettimana_gravidanza");
        for (int i = 0; i < weekValues.length; i ++) {
            System.out.println(net.getOutcomeId(HospitalConstants.PREGNANCY_WEEK_NODE, i) + " = " + (int) (weekValues[i] * 100) + "%");
        }

        double[] birthValues = net.getNodeValue("Parto");
        System.out.println("__________________\nParto");
        for (int i = 0; i < birthValues.length; i ++) {
            System.out.println(net.getOutcomeId("Parto", i) + " = " + (int) (birthValues[i] * 100) + "%");
        }

        double[] distanceValues = net.getNodeValue(HospitalConstants.DISTANCE_NODE);
        System.out.println("__________________\nDistanza");
        for (int i = 0; i < distanceValues.length; i ++) {
            System.out.println(net.getOutcomeId(HospitalConstants.DISTANCE_NODE, i) + " = " + (int) (distanceValues[i] * 100) + "%");
        }

        double[] decisionValues = net.getNodeValue(HospitalConstants.HOSPITAL_DECISION_NODE);
        System.out.println("__________________\nOspedale");
        for (int i = 0; i < decisionValues.length; i ++) {
            System.out.println(net.getOutcomeId(HospitalConstants.HOSPITAL_DECISION_NODE, i) + " = " + decisionValues[i]);
        }
        System.out.println("__________________");
    }

    private void sendNotifications(Network net) {
        double[] decisionValues = net.getNodeValue(HospitalConstants.HOSPITAL_DECISION_NODE);

        if (decisionValues[2] >= decisionValues[0] && decisionValues[2] >= decisionValues[1]) {
            System.out.println("Meglio andare");
            //sendMessage(HospitalConstants.GO);
        }
        else if (decisionValues[1] >= decisionValues[0] && decisionValues[1] > decisionValues[2]) {
            System.out.println("Meglio prepararsi");
            //sendMessage(HospitalConstants.PREPARE);
        }
        else
            System.out.println("Meglio stare a casa");
    }

    private void sendMessage(String message) {
        try {
            //GET del tipo di notifiche che la paziente vuole ricevere e settaggio appropriato dei campi boolean
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (emailNotification)
            Emailer.sendEmail(message);

        /*if (SMSNotification)
            SMSNotificator.sendSMS(message);*/

        sendDBMessage(message);
    }

    private void sendDBMessage(String m) {
        //POST del messaggio su database
    }
}