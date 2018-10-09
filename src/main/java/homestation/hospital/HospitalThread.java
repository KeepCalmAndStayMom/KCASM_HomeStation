package homestation.hospital;

import homestation.fitbit.SamplingHeartbeat;
import smile.Network;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class HospitalThread extends Thread {

    //private LocalDate gravidanza1 = LocalDate.parse("2018-10-01"); //meno di 26
    //private LocalDate gravidanza2 = LocalDate.parse("2018-09-01"); //tra 26 e 32
    private LocalDate gravidanza3 = LocalDate.parse("2018-02-01"); //tra 32 e 38
    //private LocalDate gravidanza4 = LocalDate.parse("2017-10-04"); //più di 38
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private int nContrNessuna, nContrFalsa, nContrModerata, nContrForte;

    private Network net;

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

        net = new Network();
        net.readFile("src/main/resources/rete_parto.xdsl");

        //tutto quello che segue va inglobato in un loop che si ripeterà ogni ora/mezz'ora
        //while (true) {
            //chiedo a Fitbit i dati dell'ultima mezz'ora (qui ci sono liste già pronte per i test ma nell'applicazione reale uso la API di Fitbit)
            ArrayList<SamplingHeartbeat> l = new ArrayList<>();
            //new CreateSamplingHeartbeatTestList().createList1(l);
            //new CreateSamplingHeartbeatTestList().createList2(l);
            //new CreateSamplingHeartbeatTestList().createList3(l);
            //new CreateSamplingHeartbeatTestList().createList4(l);
            //new CreateSamplingHeartbeatTestList().createList5(l);
            new CreateSamplingHeartbeatTestList().createRealisticList1(l);

            calcoloContrazione(l);

            //calcolo la distanza: uso la API di geocoding per ottenere le coordinate, poi uso la API di routing passando le coordinate per ottenere la distanza

            //calcolo la EU di andare e non andare in ospedale, se è meglio andare il bot di Telegram manda una notifica
            //net.updateBeliefs();

            //pulisco le evidenze nella rete, dopodiché attendo mezz'ora e poi ricomincio
            //net.clearAllEvidence();

            try {
                Thread.sleep(3000);
                System.out.println("Riparte il campionamento ecc");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //}

    }

    private void calcoloContrazione(ArrayList<SamplingHeartbeat> l) {
        //la data di inizio gravidanza va estratta da JSON (API), se è troppo presto andrà settata l'evidenza con nessunaContrazione
        int settimanaGravidanza = (int) ChronoUnit.DAYS.between(gravidanza3, LocalDate.now()) / 7;
        if (settimanaGravidanza < HospitalConstants.STIMA_INIZIO_CONTRAZIONI) {
            System.out.println("È ancora troppo presto per le contrazioni");
            return;
        }
        //System.out.println(settimanaGravidanza);
        //System.out.println(l);

        boolean presenzaTask = false; //usare API con categoria ginnastica e data di oggi
        SamplingHeartbeat cmpPrecedente = null;
        String contrazioneRilevata;
        ArrayList<ArrayList<SamplingHeartbeat>> listaValutazione = new ArrayList<ArrayList<SamplingHeartbeat>>();
        ArrayList<SamplingHeartbeat> lTmp = new ArrayList<SamplingHeartbeat>();

        resetCounters();

        for (SamplingHeartbeat c : l) {
            if (c.heartbeat == null) {
                //System.out.println("trovato battito nullo, salto");
                continue;
            }

            if (cmpPrecedente == null) {
                cmpPrecedente = c;
                lTmp.add(c);
                //System.out.println("trovato primo campionamento con battito non nullo");
                continue;
            }

            if ((cmpPrecedente.heartbeat > HospitalConstants.SOGLIA_MINIMA && c.heartbeat <= HospitalConstants.SOGLIA_MINIMA) || (cmpPrecedente.heartbeat <= HospitalConstants.SOGLIA_MINIMA && c.heartbeat > HospitalConstants.SOGLIA_MINIMA)) {
                listaValutazione.add(new ArrayList<SamplingHeartbeat>(lTmp));
                lTmp.clear();
                lTmp.add(c);
                //System.out.println(l_tmp + " primo if");
                //System.out.println(lista_valutazione);
            }
            else
            {
                if ((int) ChronoUnit.SECONDS.between(LocalTime.parse(cmpPrecedente.date, formatter), LocalTime.parse(c.date, formatter)) > HospitalConstants.SALTO_MASSIMO) {
                    listaValutazione.add(new ArrayList<SamplingHeartbeat>(lTmp));
                    lTmp.clear();
                    //System.out.println("campionamenti troppo distanti, aggiunta lista da valutare");
                }
                lTmp.add(c);
                //System.out.println(l_tmp + " secondo if");
                //System.out.println(lista_valutazione);
            }

            cmpPrecedente = c;
        }

        listaValutazione.add(lTmp);

        System.out.println(listaValutazione);
        System.out.println(settimanaGravidanza + " settimane, presenza task: " + presenzaTask);

        ArrayList<SamplingHeartbeat> lcPrecedente = null;
        int durataPrecedente = 0, mediaPrecedente = 0;

        for (ArrayList<SamplingHeartbeat> lc : listaValutazione) {
            if (lc.size() <= HospitalConstants.TROPPO_POCHI_CAMPIONAMENTI) {
                System.out.println("non abbastanza campionamenti da valutare");
                continue;
            }

            if (lcPrecedente == null) {
                System.out.println("trovata prima lista valutabile");

                if (media(lc) <= HospitalConstants.SOGLIA_MINIMA) {
                    System.out.println("sotto la soglia, valutata come nessuna contrazione");
                    nContrNessuna++;
                }
                else {
                    durataPrecedente = (int) ChronoUnit.SECONDS.between(LocalTime.parse(lc.get(0).date, formatter), LocalTime.parse(lc.get(lc.size()-1).date, formatter));
                    mediaPrecedente = media(lc);
                    System.out.println(durataPrecedente + " secondi, " + mediaPrecedente + " battiti");

                    if (mediaPrecedente <= HospitalConstants.SOGLIA_GINNASTICA) {
                        if (presenzaTask && (ginnastica(lc.get(0).date) || ginnastica(lc.get(lc.size()-1).date))) {
                            System.out.println("no contrazione, causa ginnastica");
                            nContrNessuna++;
                        }
                        else {
                            if (settimanaGravidanza >= 32 && durataPrecedente >= 30 && durataPrecedente <= 90) {
                                System.out.println("media intensità, SOTTO soglia ginnastica");
                                lcPrecedente = lc;
                                nContrModerata++;
                            }
                            else {
                                System.out.println("falsa contrazione, SOTTO soglia ginnastica");
                                lcPrecedente = lc;
                                nContrFalsa++;
                            }
                        }
                    }
                    else {
                        if (settimanaGravidanza >= 32) {
                            if (durataPrecedente >= 30 && durataPrecedente <= 90 && mediaPrecedente <= HospitalConstants.SOGLIA_DOLORE) {
                                System.out.println("media intensità, SOPRA soglia ginnastica");
                                lcPrecedente = lc;
                                nContrModerata++;
                            }
                            else if (durataPrecedente >= 15 && durataPrecedente <= 30 && mediaPrecedente > HospitalConstants.SOGLIA_DOLORE || settimanaGravidanza >= 38) {
                                System.out.println("forte intensità");
                                lcPrecedente = lc;
                                nContrForte++;
                            }
                            else {
                                System.out.println("falsa contrazione");
                                nContrFalsa++;
                            }
                        }
                        else {
                            System.out.println("falsa contrazione, SOPRA soglia ginnastica");
                            nContrFalsa++;
                        }
                    }
                }
                continue;
            }

            int mediaAttuale = media(lc);

            if (mediaAttuale <= HospitalConstants.SOGLIA_MINIMA) {
                System.out.println("sotto la soglia, valutata come nessuna contrazione");
                nContrNessuna++;
            }
            else {
                int durataAttuale = (int) ChronoUnit.SECONDS.between(LocalTime.parse(lc.get(0).date, formatter), LocalTime.parse(lc.get(lc.size()-1).date, formatter));
                System.out.println(durataAttuale + " secondi, " + mediaAttuale + " battiti");

                if (mediaAttuale <= HospitalConstants.SOGLIA_GINNASTICA) {
                    if (presenzaTask && (ginnastica(lc.get(0).date) || ginnastica(lc.get(lc.size()-1).date))) {
                        System.out.println("no contrazione, causa ginnastica");
                        nContrNessuna++;
                    }
                    else {
                        if (settimanaGravidanza >= 32 && durataAttuale >= 30 && durataAttuale <= 90) {
                            System.out.println("media intensità, SOTTO soglia ginnastica");
                            nContrModerata++;
                            int frequenza = (int) ChronoUnit.MINUTES.between(LocalTime.parse(lcPrecedente.get(0).date, formatter), LocalTime.parse(lc.get(0).date, formatter));

                            if (!(durataPrecedente >= 30 && durataPrecedente <= 90  && mediaPrecedente <= HospitalConstants.SOGLIA_DOLORE && frequenza >= 5 && frequenza <= 10)) {
                                System.out.println("falsa contrazione per vari motivi, SOTTO soglia ginnastica");
                                nContrFalsa++;
                            }

                            durataPrecedente = durataAttuale;
                            mediaPrecedente = mediaAttuale;
                            lcPrecedente = lc;
                        }
                        else {
                            System.out.println("falsa contrazione, SOTTO soglia ginnastica");
                            nContrFalsa++;
                        }
                    }
                }
                else {
                    if (settimanaGravidanza >= 32) {
                        if (durataAttuale >= 30 && durataAttuale <= 90 && mediaAttuale <= HospitalConstants.SOGLIA_DOLORE) {
                            System.out.println("media intensità, SOPRA soglia ginnastica");
                            nContrModerata++;
                            int frequenza = (int) ChronoUnit.MINUTES.between(LocalTime.parse(lcPrecedente.get(0).date, formatter), LocalTime.parse(lc.get(0).date, formatter));

                            if (!(durataPrecedente >= 30 && durataPrecedente <= 90  && mediaPrecedente <= HospitalConstants.SOGLIA_DOLORE && frequenza >= 5 && frequenza <= 10)) {
                                System.out.println("falsa contrazione per vari motivi, SOPRA soglia ginnastica");
                                nContrFalsa++;
                            }

                            durataPrecedente = durataAttuale;
                            mediaPrecedente = mediaAttuale;
                            lcPrecedente = lc;
                        }
                        else if (durataAttuale >= 15 && durataAttuale <= 30 && mediaAttuale > HospitalConstants.SOGLIA_DOLORE || settimanaGravidanza >= 38) {
                            System.out.println("forte intensità");
                            nContrForte++;
                            int frequenza = (int) ChronoUnit.MINUTES.between(LocalTime.parse(lcPrecedente.get(0).date, formatter), LocalTime.parse(lc.get(0).date, formatter));

                            if (!(durataPrecedente >= 15 && durataPrecedente <= 30  && mediaPrecedente > HospitalConstants.SOGLIA_DOLORE && frequenza >= 3 && frequenza <= 4)) {
                                System.out.println("falsa contrazione per vari motivi, SOPRA soglia ginnastica");
                                nContrFalsa++;
                            }

                            durataPrecedente = durataAttuale;
                            mediaPrecedente = mediaAttuale;
                            lcPrecedente = lc;
                        }
                        else {
                            System.out.println("falsa contrazione");
                            nContrFalsa++;
                        }
                    }
                    else {
                        System.out.println("falsa contrazione, SOPRA soglia ginnastica");
                        nContrFalsa++;
                    }
                }
            }
        }

        //net.setEvidence("Contrazione", misuraTipoContrazione());
        contrazioneRilevata = misuraTipoContrazione();
        System.out.println("nessuna: " + nContrNessuna);
        System.out.println("falsa: " + nContrFalsa);
        System.out.println("moderata: " + nContrModerata);
        System.out.println("forte: " + nContrForte);
        System.out.println(contrazioneRilevata);
    }

    private int media(ArrayList<SamplingHeartbeat> lcmp) {
        int media = 0, dim = lcmp.size();

        for (SamplingHeartbeat c : lcmp)
            media += c.heartbeat;

        return media / dim;
    }

    private boolean ginnastica(String data) {
        return (data.compareTo(HospitalConstants.ACTIVITIES_START_MORNING) >= 0 && data.compareTo(HospitalConstants.ACTIVITIES_END_MORNING) <= 0 || data.compareTo(HospitalConstants.ACTIVITIES_START_AFTERNOON) >= 0 && data.compareTo(HospitalConstants.ACTIVITIES_END_AFTERNOON) <= 0);
    }

    private void resetCounters() {
        nContrNessuna = 0;
        nContrFalsa = 0;
        nContrModerata = 0;
        nContrForte = 0;
    }

    private String misuraTipoContrazione() {
        if (nContrFalsa == 0 && nContrModerata == 0 && nContrForte == 0) {
            if (nContrNessuna > 0)
                return HospitalConstants.NO_CONTRACTION;
            else
                return HospitalConstants.NO_FITBIT;
        }

        if (nContrFalsa > (nContrModerata + nContrForte))
            return HospitalConstants.FALSE_CONTRACTION;
        else {
            if (nContrModerata == nContrForte)
                return HospitalConstants.REAL_MIXED_CONTRACTION;
            else if (nContrModerata > nContrForte)
                return HospitalConstants.REAL_MODERATE_CONTRACTION;
            else
                return HospitalConstants.REAL_STRONG_CONTRACTION;
        }
    }
}