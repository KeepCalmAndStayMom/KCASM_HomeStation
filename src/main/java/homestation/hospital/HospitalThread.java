package homestation.hospital;

import homestation.fitbit.SamplingHeartbeat;
import smile.Network;

import java.util.ArrayList;
import java.util.Arrays;

public class HospitalThread extends Thread {

    @Override
    public void run() {
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

        System.setProperty("jsmile.native.library", "C:/Users/Paolo/IdeaProjects/KCASM_HomeStation/lib/jsmile.dll");

        Network net = new Network();
        //net.readFile("src/main/resources/rete_parto.xdsl");
        net.readFile("src/main/resources/rete_parto.xdsl");

        //while (true) {
            //chiedo a Fitbit i dati dell'ultima mezz'ora (qui ci sono liste già pronte per i test ma nell'applicazione reale uso la API di Fitbit)
            ArrayList<SamplingHeartbeat> l = new ArrayList<>();
            //CreateSamplingHeartbeatTestList.createList1(l);
            //CreateSamplingHeartbeatTestList.createList2(l);
            //CreateSamplingHeartbeatTestList.createList3(l);
            //CreateSamplingHeartbeatTestList.createList4(l);
            //CreateSamplingHeartbeatTestList.createList5(l);
            CreateSamplingHeartbeatTestList.createRealisticList1(l);

            ContractionEvaluation.calculateContraction(l, net);

            //calcolo la distanza: uso la API di geocoding per ottenere le coordinate, poi uso la API di routing passando le coordinate per ottenere la distanza

            //calcolo la EU di andare e non andare in ospedale, se è meglio andare il bot di Telegram manda una notifica
            net.updateBeliefs();

            /*double[] contrValues = net.getNodeValue("Contrazione");
            System.out.println("__________________\nContrazione");
            for (int i = 0; i < contrValues.length; i ++) {
                System.out.println(net.getOutcomeId("Contrazione", i) + " = " + (int) (contrValues[i] * 100) + "%");
            }

            double[] birthValues = net.getNodeValue("Parto");
            System.out.println("__________________\nParto");
            for (int i = 0; i < birthValues.length; i ++) {
                System.out.println(net.getOutcomeId("Parto", i) + " = " + (int) (birthValues[i] * 100) + "%");
            }*/

            //pulisco le evidenze nella rete, dopodiché attendo mezz'ora e poi ricomincio
            net.clearAllEvidence();

            try {
                Thread.sleep(3000);
                System.out.println("Riparte il campionamento ecc");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //}

    }
}