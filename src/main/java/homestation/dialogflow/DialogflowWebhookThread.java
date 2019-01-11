package homestation.dialogflow;

import ai.api.model.AIResponse;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.*;
import ai.api.GsonFactory;
import ai.api.model.Fulfillment;
import com.google.gson.JsonElement;
import homestation.HomestationSettings;
import homestation.fitbit.Fitbit;
import homestation.fitbit.TypeDataFitBit;
import homestation.fitbit.UtilityMethodsFitbit;
import homestation.hue.HueObject;
import homestation.zway.ZWaySensor;

import static spark.Spark.*;

public class DialogflowWebhookThread extends Thread {
    private static HueObject obj;
    private static ZWaySensor zway;

    private static String text = "";

    public DialogflowWebhookThread(HueObject obj, ZWaySensor zway) {
        DialogflowWebhookThread.obj = obj;
        DialogflowWebhookThread.zway = zway;
    }

    @Override
    public void run() {
        Gson gson = GsonFactory.getDefaultFactory().getGson();
        port(4568);
        staticFiles.location("/public");

        post("/homestation",(request, response) -> {
            Fulfillment output = new Fulfillment();

            dialogflowWebhook(gson.fromJson(request.body(), AIResponse.class), output);

            response.type("application/json");

            return output;
        },gson::toJson);
    }

    private static void dialogflowWebhook(AIResponse input, Fulfillment output) {
        switch (input.getResult().getAction().toLowerCase()) {
            case "cromoterapia":
                dialogChromo(input);
                break;
            case "luci":
                dialogSwitchHues(input);
                break;
            case "luminescenza":
                text = "Ci sono " + /*zway.getLuminescence() +*/ "500 lux";
                break;
            case "temperatura":
                text = "Ci sono " + /*zway.getTemperature() +*/ "26 °C";
                break;
            case "umidità":
                text = "Umidità al " + /*zway.getHumidity() +*/ "30 %";
                break;
            case "movimento":
                //if (zway.getMovement().equalsIgnoreCase("on"))
                    text = "C'è stato movimento!";
                //else
                    //text = "Non c'è stato movimento!";
                break;
            case "generico":
                text = "Luminescenza: " + /*zway.getLuminescence() +*/ "500 lux\nTemperatura: " + /*zway.getTemperature() +*/ "26 °C\nUmidità: " + /*zway.getHumidity() +*/ "30 %\nMovimento: true" /*+ zway.getMovement()*/;
                break;
            case "battito":
                dialogHeart(input);
                break;
            /*case "tasks":
                dialogTasks(input);
                break;*/
        }

        output.setSpeech(text);
        output.setDisplayText(text);
    }

    private static void dialogSwitchHues(AIResponse input) {
        String switchHues = input.getResult().getStringParameter("any");

        if (switchHues.equalsIgnoreCase("accendi") || switchHues.equalsIgnoreCase("accendere")) {
            obj.switchOnHues();
            text = "Luci accese";
        }
        else if (switchHues.equalsIgnoreCase("spegni") || switchHues.equalsIgnoreCase("spegnere")) {
            obj.switchOffHues();
            text = "Luci spente";
        }
        else
            text = "Non ho capito la richiesta, sii più precisa!";
    }

    private static void dialogChromo(AIResponse input) {
        String cromo = input.getResult().getStringParameter("any");

        if (cromo.equalsIgnoreCase("hard") || cromo.equalsIgnoreCase("cromoterapia hard") || cromo.equalsIgnoreCase("cromo hard")) {
            new Thread(() -> obj.chromotherapyHard()).start();
            text = "cromo hard avviata!";
        }
        else if (cromo.equalsIgnoreCase("soft") || cromo.equalsIgnoreCase("cromoterapia soft") || cromo.equalsIgnoreCase("cromo soft")) {
            new Thread(() -> obj.chromotherapySoft()).start();
            text = "cromo soft partita!";
        }
        else {
            text = "Spiacente, ma non ha senso ciò che hai detto!";
        }
    }

    private static void dialogHeart(AIResponse input) {
        HashMap<String, JsonElement> m = input.getResult().getParameters();

        String[] time = m.get("time").toString().replace("[", "").replace("]", "").replace("\"", "").split(",");
        String date = m.get("date").toString().replace("\"", "");

        Fitbit f;

        if (time.length == 2) {
            if (date.equals("")) {
                /*f = UtilityMethodsFitbit.getFitbit(TypeDataFitBit.HEARTRATE, "today", time[0], time[1]);

                if (f != null && f.getAvgHeartbeats() != null)*/
                    text = "Il tuo battito cardiaco di oggi" + " dalle " + time[0] + " alle " + time[1] + " è di " + /*f.getAvgHeartbeats() +*/ "70 al minuto!";
                /*else
                    text = "Non riesco a recuperare il tuo battito cardiaco... probabilmente non stavi indossando il Fitbit!";*/
            }
            else {
                /*f = UtilityMethodsFitbit.getFitbit(TypeDataFitBit.HEARTRATE, date, time[0], time[1]);

                if (f != null && f.getAvgHeartbeats() != null)*/
                    text = "Il tuo battito cardiaco del " + date + " dalle " + time[0] + " alle " + time[1] + " era di " + /*f.getAvgHeartbeats() +*/ "70 al minuto!";
                /*else
                    text = "Non riesco a recuperare il tuo battito cardiaco... probabilmente non stavi indossando il Fitbit!";*/
            }
        }
        else {
            if (!date.equals("")) {
                /*f = UtilityMethodsFitbit.getFitbit(TypeDataFitBit.HEARTRATE, date, null, null);

                if (f != null && f.getAvgHeartbeats() != null)*/
                    text = "Il tuo battito cardiaco del " + date + " è di " + /*f.getAvgHeartbeats() +*/ "75 al minuto!";
                /*else
                    text = "Non riesco a recuperare il tuo battito cardiaco... probabilmente non stavi indossando il Fitbit!";*/
            }
            else {
                /*LocalDate localDate = LocalDate.now();
                Calendar cal = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal2.add(Calendar.MINUTE, -2);

                f = UtilityMethodsFitbit.getFitbit(TypeDataFitBit.HEARTRATE, HomestationSettings.DTF.format(localDate), HomestationSettings.SDF.format(cal2.getTime()), HomestationSettings.SDF.format(cal.getTime()));

                if (f != null && f.getAvgHeartbeats() != null)*/
                    text = "Il tuo battito cardiaco è di " + /*f.getAvgHeartbeats() +*/ "80 al minuto!";
                /*else
                    text = "Non riesco a recuperare il tuo battito cardiaco... probabilmente non stai indossando il Fitbit!";*/
            }
        }
    }
/*
    private static void dialogTasks(AIResponse input) {
        String completed = input.getResult().getStringParameter("any");

        if (completed.equalsIgnoreCase("completati")) {

            HashMap<String, Map> json = new Gson().fromJson(TaskDB.getExecutedTasks(HomestationSettings.HOMESTATION_ID), HashMap.class);

            if (json.isEmpty())
                text = "Non hai completato nessun task!";
            else {
                text = "Ecco i tasks che hai completato: \n";
                for(String key : json.keySet())
                    text += String.valueOf(json.get(key).get("title")) + " - " + String.valueOf(json.get(key).get("description")) + " - " + String.valueOf(json.get(key).get("programmed_date")) + "\n";
            }
        }
        else if (completed.equalsIgnoreCase("non completati")) {

            HashMap<String, Map> json = new Gson().fromJson(TaskDB.getTasksToExecute(HomestationSettings.HOMESTATION_ID), HashMap.class);

            System.out.println(json);

            if (json.isEmpty())
                text = "Tutti i tasks sono stati completati! Complimenti!";
            else {
                text = "Ecco i tasks che non hai ancora completato: \n";
                for (String key : json.keySet())
                    text += String.valueOf(json.get(key).get("title")) + " - " + String.valueOf(json.get(key).get("description")) + " - " + String.valueOf(json.get(key).get("programmed_date")) + "\n";
            }
        }
        else {

            HashMap<String, Map> json = new Gson().fromJson(TaskDB.getAllTasks(HomestationSettings.HOMESTATION_ID), HashMap.class);

            System.out.println(json);

            if (json.isEmpty())
                text = "Non c'è nemmeno un task!";
            else {
                text = "Ecco i tuoi tasks: \n";
                for(String key : json.keySet())
                    text += String.valueOf(json.get(key).get("title")) + " - " + String.valueOf(json.get(key).get("description")) + " - " + String.valueOf(json.get(key).get("programmed_date")) + " - " + Boolean.valueOf(String.valueOf(json.get(key).get("executed"))) + "\n";
            }
        }
    }
*/
}