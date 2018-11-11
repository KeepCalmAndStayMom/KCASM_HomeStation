package homestation.hospital;

import homestation.fitbit.SamplingHeartbeat;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class AllFactorsCounterEvaluationStrategy implements SamplingListEvaluationStrategy {
    private int nNoContr, nFalseContr, nModerateContr, nStrongContr;
    private ArrayList<SamplingHeartbeat> previousList;
    private int previousDuration, previousMean;
    private String previousType;

    @Override
    public String contractionTypeEvaluation(ArrayList<ArrayList<SamplingHeartbeat>> evaluationList, int pregnancyWeek) {
        if (evaluationList == null || evaluationList.size() == 0)
            return HospitalConstants.NO_FITBIT;

        if (pregnancyWeek < HospitalConstants.CONTRACTION_BEGINNING_ESTIMATION)
            return HospitalConstants.NO_CONTRACTION;

        boolean taskPresence = checkActivitiesInTheDay();

        resetCountersAndPrevious();

        System.out.println(evaluationList);
        System.out.println(pregnancyWeek + " settimane, presenza task: " + taskPresence);

        for (ArrayList<SamplingHeartbeat> lc : evaluationList) {
            if (lc.size() <= HospitalConstants.TOO_FEW_SAMPLINGS)
                continue;

            if (previousList == null) {
                if (mean(lc) <= HospitalConstants.MINIMUM_THRESHOLD)
                    nNoContr++;
                else {
                    previousDuration = (int) ChronoUnit.SECONDS.between(LocalTime.parse(lc.get(0).time, HospitalConstants.FORMATTER), LocalTime.parse(lc.get(lc.size()-1).time, HospitalConstants.FORMATTER));
                    previousMean = mean(lc);

                    if (previousMean <= HospitalConstants.FITNESS_THRESHOLD) {
                        if (taskPresence && (checkFitnessTime(lc.get(0).time) || checkFitnessTime(lc.get(lc.size()-1).time)))
                            nNoContr++;
                        else {
                            if (pregnancyWeek >= HospitalConstants.ADVANCED_PREGNANCY && previousDuration >= HospitalConstants.MINIMUM_DURATION_MODERATE && previousDuration <= HospitalConstants.MAXIMUM_DURATION_MODERATE) {
                                previousList = lc;
                                previousType = "moderate";
                                nModerateContr++;
                            }
                            else
                                nFalseContr++;
                        }
                    }
                    else {
                        if (pregnancyWeek >= HospitalConstants.ADVANCED_PREGNANCY) {
                            if (previousDuration >= HospitalConstants.MINIMUM_DURATION_MODERATE && previousDuration <= HospitalConstants.MAXIMUM_DURATION_MODERATE && previousMean <= HospitalConstants.PAIN_THRESHOLD && pregnancyWeek < HospitalConstants.ALMOST_BIRTH) {
                                previousList = lc;
                                previousType = "moderate";
                                nModerateContr++;
                            }
                            else if (previousDuration >= HospitalConstants.MINIMUM_DURATION_STRONG && previousDuration <= HospitalConstants.MAXIMUM_DURATION_STRONG && previousMean > HospitalConstants.PAIN_THRESHOLD || pregnancyWeek >= HospitalConstants.ALMOST_BIRTH) {
                                previousList = lc;
                                previousType = "strong";
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
                int currentDuration = (int) ChronoUnit.SECONDS.between(LocalTime.parse(lc.get(0).time, HospitalConstants.FORMATTER), LocalTime.parse(lc.get(lc.size()-1).time, HospitalConstants.FORMATTER));

                if (currentMean <= HospitalConstants.FITNESS_THRESHOLD) {
                    if (taskPresence && (checkFitnessTime(lc.get(0).time) || checkFitnessTime(lc.get(lc.size()-1).time)))
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
                        if (currentDuration >= HospitalConstants.MINIMUM_DURATION_MODERATE && currentDuration <= HospitalConstants.MAXIMUM_DURATION_MODERATE && currentMean <= HospitalConstants.PAIN_THRESHOLD && pregnancyWeek < HospitalConstants.ALMOST_BIRTH) {
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

        System.out.println("nessuna: " + nNoContr);
        System.out.println("falsa: " + nFalseContr);
        System.out.println("moderata: " + nModerateContr);
        System.out.println("forte: " + nStrongContr);
        System.out.println(checkCounter());

        return checkCounter();
    }

    private void resetCountersAndPrevious() {
        nNoContr = 0;
        nFalseContr = 0;
        nModerateContr = 0;
        nStrongContr = 0;

        previousList = null;
        previousDuration = 0;
        previousMean = 0;
        previousType = "";
    }

    private boolean checkActivitiesInTheDay() {
        try {
            URL url = new URL(HospitalConstants.ACTIVITIES_URL + LocalDate.now());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            return conn.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }

    private void compareWithPreviousContraction(int minDuration, int maxDuration, int minFrequency, int maxFrequency, int currentDuration, int currentMean, ArrayList<SamplingHeartbeat> currentList, String currentType) {
        int frequency = (int) ChronoUnit.MINUTES.between(LocalTime.parse(previousList.get(0).time, HospitalConstants.FORMATTER), LocalTime.parse(currentList.get(0).time, HospitalConstants.FORMATTER));

        if (previousType.equalsIgnoreCase(currentType)) {
            if (previousDuration >= minDuration && previousDuration <= maxDuration && frequency >= minFrequency && frequency <= maxFrequency) {
                switch (currentType) {
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
        }
        else
            previousType = currentType;

        previousDuration = currentDuration;
        previousMean = currentMean;
        previousList = currentList;
    }

    private int mean(ArrayList<SamplingHeartbeat> lcmp) {
        int mean = 0, dim = lcmp.size();

        for (SamplingHeartbeat c : lcmp)
            mean += c.heartbeat;

        return mean / dim;
    }

    private boolean checkFitnessTime(String date) {
        return (date.compareTo(HospitalConstants.ACTIVITIES_START_MORNING) >= 0 && date.compareTo(HospitalConstants.ACTIVITIES_END_MORNING) <= 0 || date.compareTo(HospitalConstants.ACTIVITIES_START_AFTERNOON) >= 0 && date.compareTo(HospitalConstants.ACTIVITIES_END_AFTERNOON) <= 0);
    }

    private String checkCounter() {
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