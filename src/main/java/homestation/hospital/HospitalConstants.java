package homestation.hospital;

import homestation.HomestationSettings;

import java.time.format.DateTimeFormatter;

class HospitalConstants {
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    final static int MINIMUM_THRESHOLD = 105;//heartbeats
    final static int FITNESS_THRESHOLD = 130;//heartbeats
    final static int PAIN_THRESHOLD = 145;//heartbeats

    final static int MAXIMUM_SKIP_BETWEEN_SAMPLINGS = 120;//seconds
    final static int TOO_FEW_SAMPLINGS = 1;//samplings in a list to evaluate
    final static int CONTRACTION_EVALUATION_FREQUENCY = 5000;//milliseconds

    final static int CONTRACTION_BEGINNING_ESTIMATION = 26;//weeks
    final static int ADVANCED_PREGNANCY = 32;//weeks
    final static int ALMOST_BIRTH = 38;//weeks

    final static int MINIMUM_FREQUENCY_MODERATE = 5;//minutes
    final static int MAXIMUM_FREQUENCY_MODERATE = 10;//minutes
    final static int MINIMUM_FREQUENCY_STRONG = 3;//minutes
    final static int MAXIMUM_FREQUENCY_STRONG = 4;//minutes

    final static int MINIMUM_DURATION_MODERATE = 31;//seconds
    final static int MAXIMUM_DURATION_MODERATE = 90;//seconds
    final static int MINIMUM_DURATION_STRONG = 15;//seconds
    final static int MAXIMUM_DURATION_STRONG = 30;//seconds

    final static double NEAR = 3.0;//kilometers
    final static double FAR = 10.0;//kilometers

    //time of the day in which people usually train
    final static String ACTIVITIES_START_MORNING = "09:00:00";
    final static String ACTIVITIES_END_MORNING = "11:00:00";
    final static String ACTIVITIES_START_AFTERNOON = "14:00:00";
    final static String ACTIVITIES_END_AFTERNOON = "18:00:00";

    //IDs and evidences in the net
    final static String HOSPITAL_DECISION_NODE = "Ospedale";

    final static String CONTRACTION_NODE = "Contrazione";
    final static String NO_CONTRACTION = "Nessuna";
    final static String NO_FITBIT = "No_fitbit";
    final static String FALSE_CONTRACTION = "Braxton_Hicks";
    final static String REAL_MODERATE_CONTRACTION = "Reale_moderata";
    final static String REAL_STRONG_CONTRACTION = "Reale_forte";
    final static String REAL_MIXED_CONTRACTION = "Reale_mista";

    final static String PREGNANCY_WEEK_NODE = "Settimana_gravidanza";
    final static String STARTING_PREGNANCY = "Meno_di_26";
    final static String EARLY_PREGNANCY = "Tra_26_e_32";
    final static String LATE_PREGNANCY = "Tra_32_e_38";
    final static String ENDING_PREGNANCY = "Oltre_38";

    final static String DISTANCE_NODE = "Distanza";
    final static String SHORT_DISTANCE = "Meno_di_3_kilometri";
    final static String MEDIUM_DISTANCE = "Tra_3_e_10_kilometri";
    final static String LONG_DISTANCE = "Oltre_10_kilometri";

    //Activities API URL
    final static String ACTIVITIES_URL = "http://localhost:4567/api/activities/" + HomestationSettings.HOMESTATION_ID + "/";

    //URLs for graphhopper APIs
    final private static String GRAPHHOPPER_API_KEY = "bf9cc82a-9a44-47a0-91c1-f5172afbf136";
    final private static String GRAPHHOPPER_URL_START = "https://graphhopper.com/api/1/";
    final static String GEO_API_PARAMS_START = GRAPHHOPPER_URL_START + "geocode?q=";
    final static String GEO_API_PARAMS_END = "&locale=it&limit=1&key=" + GRAPHHOPPER_API_KEY;
    final static String ROUTE_API_PARAMS_START = GRAPHHOPPER_URL_START + "route?point=";
    final static String ROUTE_API_PARAMS_END = "&calc_points=false&instructions=false&locale=it&key=" + GRAPHHOPPER_API_KEY;

    //notification messages
    final private static String END_MESSAGE = "\n\nNon rispondere a questo messaggio di sistema.\n\n";
    final static String GO = "Il parto è imminente! Vai in ospedale!" + END_MESSAGE;
    final static String PREPARE = "È conveniente iniziare a preparare le valigie, il parto è probabile!" + END_MESSAGE;
    final static String SUBJECT = "Notifica parto";
}