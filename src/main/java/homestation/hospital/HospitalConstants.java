package homestation.hospital;

import homestation.HomestationSettings;

class HospitalConstants {
    final static int MINIMUM_THRESHOLD = 105;//heartbeats
    final static int FITNESS_THRESHOLD = 130;//heartbeats
    final static int PAIN_THRESHOLD = 145;//heartbeats

    final static int MAXIMUM_SKIP_BETWEEN_SAMPLINGS = 120;//seconds
    final static int TOO_FEW_SAMPLINGS = 1;//samplings in a list to evaluate
    final static int SAMPLING_FREQUENCY = 5000;//milliseconds

    final static int CONTRACTION_BEGINNING_ESTIMATION = 26;//weeks
    final static int ADVANCED_PREGNANCY = 32;//weeks
    final static int ALMOST_BIRTH = 38;//weeks

    final static int MINIMUM_FREQUENCY_MODERATE = 5;//minutes
    final static int MAXIMUM_FREQUENCY_MODERATE = 10;//minutes
    final static int MINIMUM_FREQUENCY_STRONG = 3;//minutes
    final static int MAXIMUM_FREQUENCY_STRONG = 4;//minutes

    final static int MINIMUM_DURATION_MODERATE = 30;//seconds
    final static int MAXIMUM_DURATION_MODERATE = 90;//seconds
    final static int MINIMUM_DURATION_STRONG = 15;//seconds
    final static int MAXIMUM_DURATION_STRONG = 30;//seconds

    //time of the day in which people usually train
    final static String ACTIVITIES_START_MORNING = "09:00:00";
    final static String ACTIVITIES_END_MORNING = "11:00:00";
    final static String ACTIVITIES_START_AFTERNOON = "14:00:00";
    final static String ACTIVITIES_END_AFTERNOON = "18:00:00";

    //evidences in the net
    final static String NO_CONTRACTION = "Nessuna";
    final static String NO_FITBIT = "No_fitbit";
    final static String FALSE_CONTRACTION = "Braxton_Hicks";
    final static String REAL_MODERATE_CONTRACTION = "Reale_moderata";
    final static String REAL_STRONG_CONTRACTION = "Reale_forte";
    final static String REAL_MIXED_CONTRACTION = "Reale mista";

    //urls for our APIs
    final private static String API_URL_BEGINNING = "http://localhost:4567/api/";
    final static String USER_URL = API_URL_BEGINNING + "users/" + HomestationSettings.HOMESTATION_ID;
    final static String INITIAL_DATE_URL = API_URL_BEGINNING + "users/initial_date/" + HomestationSettings.HOMESTATION_ID;
    final static String ACTIVITIES_URL = API_URL_BEGINNING + "activities/" + HomestationSettings.HOMESTATION_ID + "/";

    //urls for graphhopper APIs
    final private static String GRAPHHOPPER_API_KEY = "bf9cc82a-9a44-47a0-91c1-f5172afbf136";
    final private static String GRAPHHOPPER_URL_START = "https://graphhopper.com/api/1/";
    final static String GEO_API_PARAMS_START = GRAPHHOPPER_URL_START + "geocode?q=";
    final static String GEO_API_PARAMS_END = "&locale=it&limit=1&key=" + GRAPHHOPPER_API_KEY;
    final static String ROUTE_API_PARAMS_START = GRAPHHOPPER_URL_START + "route?point=";
    final static String ROUTE_API_PARAMS_END = "&calc_points=false&instructions=false&locale=it&key=" + GRAPHHOPPER_API_KEY;
}