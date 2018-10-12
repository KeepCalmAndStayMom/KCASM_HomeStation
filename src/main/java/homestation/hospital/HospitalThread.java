package homestation.hospital;

import com.google.gson.Gson;
import homestation.HomestationSettings;
import homestation.fitbit.SamplingHeartbeat;
import smile.Network;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class HospitalThread extends Thread {

    private LocalDate PREGNANCY_START = null;

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

        setPregnancyStart();

        while (true) {
            //chiedo a Fitbit i dati dell'ultima mezz'ora (qui ci sono liste già pronte per i test ma nell'applicazione reale uso la API di Fitbit)
            ArrayList<SamplingHeartbeat> l = new ArrayList<>();
            Random rand = new Random();
            int randomTest = rand.nextInt(6) + 1;
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
                default:
                    CreateSamplingHeartbeatTestList.createRealisticList1(l);
                    break;
            }
            ContractionEvaluation.calculateContraction(l, net, PREGNANCY_START);

            //calcolo la distanza: uso la API di geocoding per ottenere le coordinate, poi uso la API di routing passando le coordinate per ottenere la distanza
            DistanceEvaluation.calculateDistance(net);

            //calcolo la EU di andare e non andare in ospedale, se è meglio andare il bot di Telegram manda una notifica
            net.updateBeliefs();

            //printValues(net);

            //pulisco le evidenze nella rete, dopodiché attendo mezz'ora e poi ricomincio
            net.clearAllEvidence();

            try {
                Thread.sleep(HospitalConstants.SAMPLING_FREQUENCY);
                System.out.println("Riparte il campionamento ecc");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printValues(Network net) {
        double[] contrValues = net.getNodeValue("Contrazione");
            System.out.println("__________________\nContrazione");
            for (int i = 0; i < contrValues.length; i ++) {
                System.out.println(net.getOutcomeId("Contrazione", i) + " = " + (int) (contrValues[i] * 100) + "%");
            }

            double[] birthValues = net.getNodeValue("Parto");
            System.out.println("__________________\nParto");
            for (int i = 0; i < birthValues.length; i ++) {
                System.out.println(net.getOutcomeId("Parto", i) + " = " + (int) (birthValues[i] * 100) + "%");
            }

            double[] distanceValues = net.getNodeValue("Distanza");
            System.out.println("__________________\nDistanza");
            for (int i = 0; i < distanceValues.length; i ++) {
                System.out.println(net.getOutcomeId("Distanza", i) + " = " + (int) (distanceValues[i] * 100) + "%");
            }

            double[] decisionValues = net.getNodeValue("Ospedale");
            System.out.println("__________________\nOspedale");
            for (int i = 0; i < decisionValues.length; i ++) {
                System.out.println(net.getOutcomeId("Ospedale", i) + " = " + (int) (decisionValues[i] * 100));
            }
            System.out.println("__________________");
    }

    //poiché nel DB la data di inizio gravidanza non può essere null, verrà sempre trovata
    private void setPregnancyStart() {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(HospitalConstants.INITIAL_DATE_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            HashMap map = new Gson().fromJson(result.toString(), HashMap.class);
            PREGNANCY_START = LocalDate.parse((String) map.get("data_inizio_gravidanza"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}