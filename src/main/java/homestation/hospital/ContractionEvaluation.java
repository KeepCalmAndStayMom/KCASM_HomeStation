package homestation.hospital;

import homestation.fitbit.SamplingHeartbeat;
import smile.Network;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

class ContractionEvaluation {
    /*
    private static int gravidanza1 = 22; //prima di 26esima
    private static int gravidanza2 = 28; //tra 26esima e 32esima
    private static int gravidanza3 = 34; //tra 32esima e 38esima
    private static int gravidanza4 = 40; //dopo la 38esima
    */
    static void calculateContraction(ArrayList<SamplingHeartbeat> l, Network net, LocalDate startPregnancy, SamplingListScanStrategy scanStrategy, SamplingListEvaluationStrategy evaluationStrategy) {
        int pregnancyWeek = (int) ChronoUnit.WEEKS.between(startPregnancy, LocalDate.now());

        //int pregnancyWeek = gravidanza1;
        //int pregnancyWeek = gravidanza2;
        //int pregnancyWeek = gravidanza3;
        //int pregnancyWeek = gravidanza4;

        if (pregnancyWeek < HospitalConstants.CONTRACTION_BEGINNING_ESTIMATION) {
            net.setEvidence(HospitalConstants.CONTRACTION_NODE, HospitalConstants.NO_CONTRACTION);
            net.setEvidence(HospitalConstants.PREGNANCY_WEEK_NODE, HospitalConstants.STARTING_PREGNANCY);
            return;
        }

        pregnancyTimeEvaluation(net, pregnancyWeek);
        
        net.setEvidence(HospitalConstants.CONTRACTION_NODE, evaluationStrategy.contractionTypeEvaluation(scanStrategy.createEvaluationList(l), pregnancyWeek));
    }

    private static void pregnancyTimeEvaluation(Network net, int pregnancyWeek) {
        if (pregnancyWeek >= HospitalConstants.CONTRACTION_BEGINNING_ESTIMATION && pregnancyWeek < HospitalConstants.ADVANCED_PREGNANCY)
            net.setEvidence(HospitalConstants.PREGNANCY_WEEK_NODE, HospitalConstants.EARLY_PREGNANCY);
        else if (pregnancyWeek >= HospitalConstants.ADVANCED_PREGNANCY && pregnancyWeek < HospitalConstants.ALMOST_BIRTH)
            net.setEvidence(HospitalConstants.PREGNANCY_WEEK_NODE, HospitalConstants.LATE_PREGNANCY);
        else
            net.setEvidence(HospitalConstants.PREGNANCY_WEEK_NODE, HospitalConstants.ENDING_PREGNANCY);
    }
}