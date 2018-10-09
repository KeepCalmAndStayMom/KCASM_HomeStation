package homestation.hospital;

import homestation.fitbit.SamplingHeartbeat;
import smile.Network;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

class ContractionEvaluation {
    
    //private static LocalDate gravidanza1 = LocalDate.parse("2018-10-01"); //meno di 26
    //private static LocalDate gravidanza2 = LocalDate.parse("2018-09-01"); //tra 26 e 32
    private static LocalDate gravidanza3 = LocalDate.parse("2018-02-01"); //tra 32 e 38
    //private static LocalDate gravidanza4 = LocalDate.parse("2017-10-01"); //più di 38
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static int nNoContr = 0, nFalseContr = 0, nModerateContr = 0, nStrongContr = 0;

    private static ArrayList<SamplingHeartbeat> previousList = null;
    private static int previousDuration = 0, previousMean = 0;
    
    static void calculateContraction(ArrayList<SamplingHeartbeat> l, Network net) {
        //la data di inizio gravidanza va estratta da JSON (API), se è troppo presto andrà settata l'evidenza con nessunaContrazione
        int pregnancyWeek = (int) ChronoUnit.DAYS.between(gravidanza3, LocalDate.now()) / HospitalConstants.WEEK_SIZE;
        if (pregnancyWeek < HospitalConstants.CONTRACTION_BEGINNING_ESTIMATION) {
            System.out.println("È ancora troppo presto per le contrazioni");
            net.setEvidence("Contrazione", "Nessuna");
            return;
        }
        //System.out.println(pregnancyWeek);

        ArrayList<ArrayList<SamplingHeartbeat>> evaluationList = samplingScan(l);

        System.out.println(evaluationList);

        samplingEvaluation(evaluationList, pregnancyWeek);
        
        net.setEvidence("Contrazione", misuraTipoContrazione());
        
        System.out.println("nessuna: " + nNoContr);
        System.out.println("falsa: " + nFalseContr);
        System.out.println("moderata: " + nModerateContr);
        System.out.println("forte: " + nStrongContr);
        System.out.println(misuraTipoContrazione());
    }

    private static ArrayList<ArrayList<SamplingHeartbeat>> samplingScan(ArrayList<SamplingHeartbeat> l) {
        SamplingHeartbeat previousSampling = null;
        ArrayList<ArrayList<SamplingHeartbeat>> evaluationList = new ArrayList<>();
        ArrayList<SamplingHeartbeat> lTmp = new ArrayList<>();

        for (SamplingHeartbeat c : l) {
            if (c.heartbeat == null)
                continue;

            if (previousSampling == null) {
                previousSampling = c;
                lTmp.add(c);
                continue;
            }

            if ((previousSampling.heartbeat > HospitalConstants.MINIMUM_THRESHOLD && c.heartbeat <= HospitalConstants.MINIMUM_THRESHOLD) || (previousSampling.heartbeat <= HospitalConstants.MINIMUM_THRESHOLD && c.heartbeat > HospitalConstants.MINIMUM_THRESHOLD)) {
                evaluationList.add(new ArrayList<>(lTmp));
                lTmp.clear();
                lTmp.add(c);
            }
            else {
                if ((int) ChronoUnit.SECONDS.between(LocalTime.parse(previousSampling.date, formatter), LocalTime.parse(c.date, formatter)) > HospitalConstants.MAXIMUM_SKIP_BETWEEN_SAMPLINGS) {
                    evaluationList.add(new ArrayList<>(lTmp));
                    lTmp.clear();
                }
                lTmp.add(c);
            }
            previousSampling = c;
        }

        evaluationList.add(lTmp);

        return evaluationList;
    }

    private static void samplingEvaluation(ArrayList<ArrayList<SamplingHeartbeat>> evaluationList, int pregnancyWeek) {
        boolean taskPresence = false; //usare API con categoria checkFitnessTime e data di oggi

        System.out.println(pregnancyWeek + " settimane, presenza task: " + taskPresence);

        for (ArrayList<SamplingHeartbeat> lc : evaluationList) {
            if (lc.size() <= HospitalConstants.TOO_FEW_SAMPLINGS)
                continue;

            if (previousList == null) {
                if (mean(lc) <= HospitalConstants.MINIMUM_THRESHOLD)
                    nNoContr++;
                else {
                    previousDuration = (int) ChronoUnit.SECONDS.between(LocalTime.parse(lc.get(0).date, formatter), LocalTime.parse(lc.get(lc.size()-1).date, formatter));
                    previousMean = mean(lc);

                    if (previousMean <= HospitalConstants.FITNESS_THRESHOLD) {
                        if (taskPresence && (checkFitnessTime(lc.get(0).date) || checkFitnessTime(lc.get(lc.size()-1).date)))
                            nNoContr++;
                        else {
                            if (pregnancyWeek >= HospitalConstants.ADVANCED_PREGNANCY && previousDuration >= HospitalConstants.MINIMUM_DURATION_MODERATE && previousDuration <= HospitalConstants.MAXIMUM_DURATION_MODERATE) {
                                previousList = lc;
                                nModerateContr++;
                            }
                            else {
                                previousList = lc;
                                nFalseContr++;
                            }
                        }
                    }
                    else {
                        if (pregnancyWeek >= HospitalConstants.ADVANCED_PREGNANCY) {
                            if (previousDuration > HospitalConstants.MINIMUM_DURATION_MODERATE && previousDuration <= HospitalConstants.MAXIMUM_DURATION_MODERATE && previousMean <= HospitalConstants.PAIN_THRESHOLD) {
                                previousList = lc;
                                nModerateContr++;
                            }
                            else if (previousDuration >= HospitalConstants.MINIMUM_DURATION_STRONG && previousDuration <= HospitalConstants.MAXIMUM_DURATION_STRONG && previousMean > HospitalConstants.PAIN_THRESHOLD || pregnancyWeek >= HospitalConstants.ALMOST_BIRTH) {
                                previousList = lc;
                                nStrongContr++;
                            }
                            else
                                nFalseContr++;
                        }
                        else
                            nFalseContr++;
                    }
                }
                continue;
            }

            int currentMean = mean(lc);

            if (currentMean <= HospitalConstants.MINIMUM_THRESHOLD)
                nNoContr++;
            else {
                int currentDuration = (int) ChronoUnit.SECONDS.between(LocalTime.parse(lc.get(0).date, formatter), LocalTime.parse(lc.get(lc.size()-1).date, formatter));

                if (currentMean <= HospitalConstants.FITNESS_THRESHOLD) {
                    if (taskPresence && (checkFitnessTime(lc.get(0).date) || checkFitnessTime(lc.get(lc.size()-1).date)))
                        nNoContr++;
                    else {
                        if (pregnancyWeek >= HospitalConstants.ADVANCED_PREGNANCY && currentDuration >= HospitalConstants.MINIMUM_DURATION_MODERATE && currentDuration <= HospitalConstants.MAXIMUM_DURATION_MODERATE) {
                            nModerateContr++;
                            compareWithPreviousContraction(HospitalConstants.MINIMUM_DURATION_MODERATE, HospitalConstants.MAXIMUM_DURATION_MODERATE, HospitalConstants.MINIMUM_FREQUENCY_MODERATE, HospitalConstants.MAXIMUM_FREQUENCY_MODERATE, currentDuration, currentMean, lc, "moderate");
                        }
                        else
                            nFalseContr++;
                    }
                }
                else {
                    if (pregnancyWeek >= HospitalConstants.ADVANCED_PREGNANCY) {
                        if (currentDuration > HospitalConstants.MINIMUM_DURATION_MODERATE && currentDuration <= HospitalConstants.MAXIMUM_DURATION_MODERATE && currentMean <= HospitalConstants.PAIN_THRESHOLD) {
                            nModerateContr++;
                            compareWithPreviousContraction(HospitalConstants.MINIMUM_DURATION_MODERATE, HospitalConstants.MAXIMUM_DURATION_MODERATE, HospitalConstants.MINIMUM_FREQUENCY_MODERATE, HospitalConstants.MAXIMUM_FREQUENCY_MODERATE, currentDuration, currentMean, lc, "moderate");
                        }
                        else if (currentDuration >= HospitalConstants.MINIMUM_DURATION_STRONG && currentDuration <= HospitalConstants.MAXIMUM_DURATION_STRONG && currentMean > HospitalConstants.PAIN_THRESHOLD || pregnancyWeek >= HospitalConstants.ALMOST_BIRTH) {
                            nStrongContr++;
                            compareWithPreviousContraction(HospitalConstants.MINIMUM_DURATION_STRONG, HospitalConstants.MAXIMUM_DURATION_STRONG, HospitalConstants.MINIMUM_FREQUENCY_STRONG, HospitalConstants.MAXIMUM_FREQUENCY_STRONG, currentDuration, currentMean, lc, "strong");
                        }
                        else
                            nFalseContr++;
                    }
                    else
                        nFalseContr++;
                }
            }
        }
    }

    private static void compareWithPreviousContraction(int minDuration, int maxDuration, int minFrequency, int maxFrequency, int currentDuration, int currentMean, ArrayList<SamplingHeartbeat> currentList, String type) {
        int frequency = (int) ChronoUnit.MINUTES.between(LocalTime.parse(previousList.get(0).date, formatter), LocalTime.parse(currentList.get(0).date, formatter));

        if (previousDuration >= minDuration && previousDuration <= maxDuration && frequency >= minFrequency && frequency <= maxFrequency) {
            switch (type) {
                case "strong":
                    if (!(previousMean > HospitalConstants.PAIN_THRESHOLD))
                        nFalseContr++;
                    break;
                case "moderate":
                    if (!(previousMean <= HospitalConstants.PAIN_THRESHOLD))
                        nFalseContr++;
                    break;
            }
        }
        else
            nFalseContr++;

        previousDuration = currentDuration;
        previousMean = currentMean;
        previousList = currentList;
    }

    private static int mean(ArrayList<SamplingHeartbeat> lcmp) {
        int mean = 0, dim = lcmp.size();

        for (SamplingHeartbeat c : lcmp)
            mean += c.heartbeat;

        return mean / dim;
    }

    private static boolean checkFitnessTime(String data) {
        return (data.compareTo(HospitalConstants.ACTIVITIES_START_MORNING) >= 0 && data.compareTo(HospitalConstants.ACTIVITIES_END_MORNING) <= 0 || data.compareTo(HospitalConstants.ACTIVITIES_START_AFTERNOON) >= 0 && data.compareTo(HospitalConstants.ACTIVITIES_END_AFTERNOON) <= 0);
    }

    //modificabile con void come tipo di ritorno e settaggio evidenza all'interno degli if (net diventa un parametro da passare)
    private static String misuraTipoContrazione() {
        if (nFalseContr == 0 && nModerateContr == 0 && nStrongContr == 0) {
            if (nNoContr > 0)
                return HospitalConstants.NO_CONTRACTION;
            else
                return HospitalConstants.NO_FITBIT;
        }

        if (nFalseContr > (nModerateContr + nStrongContr))
            return HospitalConstants.FALSE_CONTRACTION;
        else {
            if (nModerateContr == nStrongContr)
                return HospitalConstants.REAL_MIXED_CONTRACTION;
            else if (nModerateContr > nStrongContr)
                return HospitalConstants.REAL_MODERATE_CONTRACTION;
            else
                return HospitalConstants.REAL_STRONG_CONTRACTION;
        }
    }
}