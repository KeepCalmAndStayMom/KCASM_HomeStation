package homestation.hospital;

class HospitalConstants {
    final static int MINIMUM_THRESHOLD = 105;
    final static int FITNESS_THRESHOLD = 130;
    final static int PAIN_THRESHOLD = 145;

    final static int MAXIMUM_SKIP_BETWEEN_SAMPLINGS = 120;

    final static int TOO_FEW_SAMPLINGS = 1;

    final static int CONTRACTION_BEGINNING_ESTIMATION = 26;
    final static int ADVANCED_PREGNANCY = 32;
    final static int ALMOST_BIRTH = 38;

    final static int MINIMUM_FREQUENCY_MODERATE = 5;
    final static int MAXIMUM_FREQUENCY_MODERATE = 10;
    final static int MINIMUM_FREQUENCY_STRONG = 3;
    final static int MAXIMUM_FREQUENCY_STRONG = 4;

    final static int MINIMUM_DURATION_MODERATE = 30;
    final static int MAXIMUM_DURATION_MODERATE = 90;
    final static int MINIMUM_DURATION_STRONG = 15;
    final static int MAXIMUM_DURATION_STRONG = 30;

    final static String ACTIVITIES_START_MORNING = "09:00:00";
    final static String ACTIVITIES_END_MORNING = "11:00:00";
    final static String ACTIVITIES_START_AFTERNOON = "14:00:00";
    final static String ACTIVITIES_END_AFTERNOON = "18:00:00";

    final static String NO_CONTRACTION = "Nessuna";
    final static String NO_FITBIT = "No_fitbit";
    final static String FALSE_CONTRACTION = "Braxton_Hicks";
    final static String REAL_MODERATE_CONTRACTION = "Reale_moderata";
    final static String REAL_STRONG_CONTRACTION = "Reale_forte";
    final static String REAL_MIXED_CONTRACTION = "Reale mista";

    final static int SAMPLING_FREQUENCY = 5000;
}