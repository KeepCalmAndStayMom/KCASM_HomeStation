package homestation.hospital;

import homestation.fitbit.SamplingHeartbeat;

import java.util.ArrayList;

public class CreateSamplingHeartbeatTestList {

    /*nota 1: "sintomi di contrazione moderata/forte" vuol dire che potrebbe non essere considerata tale a causa di fattori come la settimana di gravidanza o la presenza di task...*/
    /*nota 2: i primi 5 metodi sono stati pensati per valutare le contrazioni senza tenere conto della loro regolarità*/
    /*nota 3: i metodi successivi al quinto cercano di simulare campionamenti più realistici*/

    public void createList1(ArrayList<SamplingHeartbeat> l) {//tutto sotto la soglia minima
        l.add(new SamplingHeartbeat(85, "12:00:00"));
        l.add(new SamplingHeartbeat(95, "12:00:05"));
        l.add(new SamplingHeartbeat(95, "12:00:15"));
        l.add(new SamplingHeartbeat(75, "12:00:30"));
        l.add(new SamplingHeartbeat(75, "12:00:59"));
        l.add(new SamplingHeartbeat(95, "12:01:00"));
        l.add(new SamplingHeartbeat(85, "12:01:45"));//fine prima sottolista valutabile

        l.add(new SamplingHeartbeat(95, "12:03:50"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(75, "12:07:00"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(65, "12:09:10"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(85, "12:17:12"));
        l.add(new SamplingHeartbeat(95, "12:18:38"));//fine seconda sottolista valutabile

        l.add(new SamplingHeartbeat(75, "12:20:37"));
        l.add(new SamplingHeartbeat(85, "12:28:58"));
        l.add(new SamplingHeartbeat(95, "12:29:59"));//fine terza sottolista valutabile
    }

    public void createList2(ArrayList<SamplingHeartbeat> l) {//tutto sotto la soglia ginnastica
        l.add(new SamplingHeartbeat(110, "12:00:00"));
        l.add(new SamplingHeartbeat(115, "12:00:05"));
        l.add(new SamplingHeartbeat(120, "12:00:15"));
        l.add(new SamplingHeartbeat(115, "12:00:30"));
        l.add(new SamplingHeartbeat(110, "12:00:55"));//sintomi di contrazione moderata, fuori dalla fascia di ginnastica

        l.add(new SamplingHeartbeat(95, "12:01:00"));
        l.add(new SamplingHeartbeat(85, "12:01:30"));
        l.add(new SamplingHeartbeat(95, "12:02:00"));//no contrazione

        l.add(new SamplingHeartbeat(110, "12:02:30"));
        l.add(new SamplingHeartbeat(115, "12:03:00"));
        l.add(new SamplingHeartbeat(120, "12:03:30"));
        l.add(new SamplingHeartbeat(110, "12:04:30"));//falsa contrazione, troppo lunga

        l.add(new SamplingHeartbeat(85, "12:05:00"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(110, "15:00:00"));
        l.add(new SamplingHeartbeat(115, "15:00:05"));
        l.add(new SamplingHeartbeat(120, "15:00:15"));
        l.add(new SamplingHeartbeat(115, "15:00:30"));
        l.add(new SamplingHeartbeat(110, "15:00:59"));//sintomi di contrazione moderata, ma nella fascia di ginnastica
    }

    public void createList3(ArrayList<SamplingHeartbeat> l) {//tutto sotto la soglia dolore
        l.add(new SamplingHeartbeat(135, "12:00:00"));
        l.add(new SamplingHeartbeat(140, "12:00:05"));
        l.add(new SamplingHeartbeat(145, "12:00:15"));
        l.add(new SamplingHeartbeat(140, "12:00:30"));
        l.add(new SamplingHeartbeat(135, "12:00:55"));//sintomi di contrazione moderata, sopra la soglia ginnastica

        l.add(new SamplingHeartbeat(95, "12:01:00"));
        l.add(new SamplingHeartbeat(85, "12:01:30"));
        l.add(new SamplingHeartbeat(95, "12:02:00"));//no contrazione

        l.add(new SamplingHeartbeat(110, "12:02:30"));
        l.add(new SamplingHeartbeat(115, "12:03:00"));
        l.add(new SamplingHeartbeat(120, "12:03:30"));
        l.add(new SamplingHeartbeat(110, "12:04:30"));//falsa contrazione, troppo lunga

        l.add(new SamplingHeartbeat(85, "12:05:00"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(110, "12:10:00"));
        l.add(new SamplingHeartbeat(115, "12:10:05"));
        l.add(new SamplingHeartbeat(120, "12:10:15"));
        l.add(new SamplingHeartbeat(115, "12:10:30"));
        l.add(new SamplingHeartbeat(110, "12:10:59"));//sintomi di contrazione moderata, fuori dalla fascia ginnastica

        l.add(new SamplingHeartbeat(85, "12:15:00"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(110, "12:15:30"));
        l.add(new SamplingHeartbeat(115, "12:15:35"));
        l.add(new SamplingHeartbeat(120, "12:15:40"));
        l.add(new SamplingHeartbeat(115, "12:15:50"));
        l.add(new SamplingHeartbeat(110, "12:16:05"));//sintomi di contrazione moderata, fuori dalla fascia ginnastica

        l.add(new SamplingHeartbeat(95, "12:17:00"));
        l.add(new SamplingHeartbeat(85, "12:17:30"));
        l.add(new SamplingHeartbeat(95, "12:18:00"));//no contrazione

        l.add(new SamplingHeartbeat(135, "12:20:00"));
        l.add(new SamplingHeartbeat(140, "12:20:05"));
        l.add(new SamplingHeartbeat(145, "12:20:15"));
        l.add(new SamplingHeartbeat(140, "12:20:30"));
        l.add(new SamplingHeartbeat(135, "12:20:55"));//sintomi di contrazione moderata, sopra la soglia ginnastica

        l.add(new SamplingHeartbeat(100, "12:22:00"));
        l.add(new SamplingHeartbeat(90, "12:23:00"));
        l.add(new SamplingHeartbeat(100, "12:24:00"));//no contrazione

        l.add(new SamplingHeartbeat(110, "12:25:00"));
        l.add(new SamplingHeartbeat(115, "12:25:05"));
        l.add(new SamplingHeartbeat(120, "12:25:10"));
        l.add(new SamplingHeartbeat(110, "12:25:15"));//falsa contrazione, troppo breve
    }

    public void createList4(ArrayList<SamplingHeartbeat> l) {//anche sopra la soglia dolore
        l.add(new SamplingHeartbeat(155, "11:55:00"));
        l.add(new SamplingHeartbeat(160, "11:55:05"));
        l.add(new SamplingHeartbeat(165, "11:55:10"));
        l.add(new SamplingHeartbeat(165, "11:55:15"));
        l.add(new SamplingHeartbeat(160, "11:55:20"));
        l.add(new SamplingHeartbeat(155, "11:55:25"));//sintomi di contrazione forte

        l.add(new SamplingHeartbeat(135, "12:00:00"));
        l.add(new SamplingHeartbeat(140, "12:00:05"));
        l.add(new SamplingHeartbeat(115, "12:00:15"));
        l.add(new SamplingHeartbeat(145, "12:00:30"));
        l.add(new SamplingHeartbeat(140, "12:00:45"));
        l.add(new SamplingHeartbeat(135, "12:00:55"));//sintomi di contrazione moderata, sopra la soglia ginnastica

        l.add(new SamplingHeartbeat(95, "12:01:00"));
        l.add(new SamplingHeartbeat(85, "12:01:30"));
        l.add(new SamplingHeartbeat(95, "12:02:00"));//no contrazione

        l.add(new SamplingHeartbeat(110, "12:02:30"));
        l.add(new SamplingHeartbeat(115, "12:03:00"));
        l.add(new SamplingHeartbeat(120, "12:03:30"));
        l.add(new SamplingHeartbeat(110, "12:04:30"));//falsa contrazione, troppo lunga

        l.add(new SamplingHeartbeat(85, "12:05:00"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(155, "12:10:20"));
        l.add(new SamplingHeartbeat(160, "12:10:25"));
        l.add(new SamplingHeartbeat(175, "12:10:30"));
        l.add(new SamplingHeartbeat(165, "12:10:35"));
        l.add(new SamplingHeartbeat(160, "12:10:40"));
        l.add(new SamplingHeartbeat(155, "12:10:45"));//sintomi di contrazione forte

        l.add(new SamplingHeartbeat(110, "12:15:00"));
        l.add(new SamplingHeartbeat(115, "12:15:05"));
        l.add(new SamplingHeartbeat(120, "12:15:10"));
        l.add(new SamplingHeartbeat(110, "12:15:15"));//falsa contrazione, troppo breve

        l.add(new SamplingHeartbeat(155, "12:20:00"));
        l.add(new SamplingHeartbeat(160, "12:20:05"));
        l.add(new SamplingHeartbeat(165, "12:20:10"));
        l.add(new SamplingHeartbeat(165, "12:20:15"));
        l.add(new SamplingHeartbeat(160, "12:20:20"));
        l.add(new SamplingHeartbeat(155, "12:20:25"));//sintomi di contrazione forte

        l.add(new SamplingHeartbeat(160, "12:25:30"));
        l.add(new SamplingHeartbeat(165, "12:26:00"));
        l.add(new SamplingHeartbeat(170, "12:26:30"));
        l.add(new SamplingHeartbeat(160, "12:27:30"));//falsa contrazione, troppo lunga

        l.add(new SamplingHeartbeat(110, "15:00:00"));
        l.add(new SamplingHeartbeat(115, "15:00:05"));
        l.add(new SamplingHeartbeat(120, "15:00:15"));
        l.add(new SamplingHeartbeat(115, "15:00:30"));
        l.add(new SamplingHeartbeat(110, "15:00:59"));//sintomi di contrazione moderata, ma nella fascia di ginnastica
    }

    public void createList5(ArrayList<SamplingHeartbeat> l) {//anche campionamenti con battito null
        l.add(new SamplingHeartbeat(null, "11:55:00"));
        l.add(new SamplingHeartbeat(160, "11:55:05"));
        l.add(new SamplingHeartbeat(165, "11:55:10"));
        l.add(new SamplingHeartbeat(165, "11:55:15"));
        l.add(new SamplingHeartbeat(160, "11:55:20"));
        l.add(new SamplingHeartbeat(null, "11:55:25"));//sintomi di contrazione forte

        l.add(new SamplingHeartbeat(135, "12:00:00"));
        l.add(new SamplingHeartbeat(null, "12:00:05"));
        l.add(new SamplingHeartbeat(145, "12:00:15"));
        l.add(new SamplingHeartbeat(null, "12:00:30"));
        l.add(new SamplingHeartbeat(140, "12:00:45"));
        l.add(new SamplingHeartbeat(135, "12:00:55"));//sintomi di contrazione moderata, sopra la soglia ginnastica, ma a seconda dei null successivi può diventare falsa

        l.add(new SamplingHeartbeat(null, "12:01:00"));
        l.add(new SamplingHeartbeat(null, "12:01:30"));
        l.add(new SamplingHeartbeat(null, "12:02:00"));//no contrazione, ma saltata

        l.add(new SamplingHeartbeat(110, "12:02:30"));//se questo fosse null, ci sarebbe troppa differenza di tempo tra il campionamento precedente e il successivo
        l.add(new SamplingHeartbeat(115, "12:03:00"));
        l.add(new SamplingHeartbeat(120, "12:03:30"));
        l.add(new SamplingHeartbeat(110, "12:04:45"));//falsa contrazione, troppo lunga

        l.add(new SamplingHeartbeat(85, "12:05:00"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(155, "12:10:20"));
        l.add(new SamplingHeartbeat(160, "12:10:25"));
        l.add(new SamplingHeartbeat(165, "12:10:30"));
        l.add(new SamplingHeartbeat(165, "12:10:35"));
        l.add(new SamplingHeartbeat(160, "12:10:40"));
        l.add(new SamplingHeartbeat(null, "12:10:45"));//sintomi di contrazione forte

        l.add(new SamplingHeartbeat(110, "12:15:00"));
        l.add(new SamplingHeartbeat(115, "12:15:04"));
        l.add(new SamplingHeartbeat(null, "12:15:08"));
        l.add(new SamplingHeartbeat(110, "12:15:12"));//falsa contrazione, troppo breve

        l.add(new SamplingHeartbeat(null, "12:20:00"));
        l.add(new SamplingHeartbeat(null, "12:20:05"));
        l.add(new SamplingHeartbeat(null, "12:20:10"));
        l.add(new SamplingHeartbeat(null, "12:20:15"));
        l.add(new SamplingHeartbeat(null, "12:20:20"));
        l.add(new SamplingHeartbeat(155, "12:20:25"));//sintomi di contrazione forte, ma essendo isolata per i null non è valutabile

        l.add(new SamplingHeartbeat(160, "12:25:30"));
        l.add(new SamplingHeartbeat(165, "12:26:00"));//sintomi di contrazione forte a causa dei null successivi
        l.add(new SamplingHeartbeat(null, "12:26:30"));
        l.add(new SamplingHeartbeat(null, "12:27:30"));//falsa contrazione, troppo lunga

        l.add(new SamplingHeartbeat(null, "17:59:45"));//questi null fanno uscire dalla fascia di ginnastica
        l.add(new SamplingHeartbeat(null, "17:59:50"));
        l.add(new SamplingHeartbeat(null, "17:59:55"));
        l.add(new SamplingHeartbeat(null, "17:59:59"));
        l.add(new SamplingHeartbeat(120, "18:00:01"));
        l.add(new SamplingHeartbeat(115, "18:00:20"));
        l.add(new SamplingHeartbeat(110, "18:00:40"));//sintomi di contrazione moderata
    }

    public void createRealisticList1(ArrayList<SamplingHeartbeat> l) {//principalmente sotto la soglia dolore
        l.add(new SamplingHeartbeat(80, "17:45:00"));
        l.add(new SamplingHeartbeat(84, "17:45:12"));
        l.add(new SamplingHeartbeat(82, "17:45:18"));
        l.add(new SamplingHeartbeat(78, "17:45:27"));
        l.add(new SamplingHeartbeat(81, "17:45:34"));
        l.add(new SamplingHeartbeat(88, "17:45:45"));
        l.add(new SamplingHeartbeat(85, "17:45:57"));
        l.add(new SamplingHeartbeat(81, "17:46:03"));
        l.add(new SamplingHeartbeat(84, "17:46:19"));
        l.add(new SamplingHeartbeat(89, "17:46:33"));
        l.add(new SamplingHeartbeat(92, "17:46:46"));
        l.add(new SamplingHeartbeat(97, "17:47:01"));
        l.add(new SamplingHeartbeat(99, "17:47:16"));
        l.add(new SamplingHeartbeat(98, "17:47:35"));
        l.add(new SamplingHeartbeat(101, "17:47:47"));
        l.add(new SamplingHeartbeat(104, "17:48:01"));//no contrazione

        l.add(new SamplingHeartbeat(106, "17:48:13"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(103, "17:48:46"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(107, "17:49:27"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(104, "17:49:44"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(109, "17:50:03"));
        l.add(new SamplingHeartbeat(111, "17:50:11"));//falsa contrazione, troppo breve, ma nella fascia di ginnastica

        l.add(new SamplingHeartbeat(null, "17:50:32"));
        l.add(new SamplingHeartbeat(null, "17:50:48"));
        l.add(new SamplingHeartbeat(null, "17:51:10"));
        l.add(new SamplingHeartbeat(null, "17:51:24"));
        l.add(new SamplingHeartbeat(null, "17:51:36"));
        l.add(new SamplingHeartbeat(null, "17:51:53"));
        l.add(new SamplingHeartbeat(null, "17:52:04"));
        l.add(new SamplingHeartbeat(null, "17:52:16"));
        l.add(new SamplingHeartbeat(null, "17:52:37"));//nessun campionamento per due minuti

        l.add(new SamplingHeartbeat(114, "17:52:46"));
        l.add(new SamplingHeartbeat(117, "17:52:59"));
        l.add(new SamplingHeartbeat(null, "17:53:11"));
        l.add(new SamplingHeartbeat(119, "17:53:28"));
        l.add(new SamplingHeartbeat(116, "17:53:44"));
        l.add(new SamplingHeartbeat(118, "17:53:59"));
        l.add(new SamplingHeartbeat(null, "17:54:11"));
        l.add(new SamplingHeartbeat(115, "17:54:28"));
        l.add(new SamplingHeartbeat(114, "17:54:46"));
        l.add(new SamplingHeartbeat(113, "17:54:58"));
        l.add(new SamplingHeartbeat(null, "17:55:09"));
        l.add(new SamplingHeartbeat(107, "17:55:21"));//falsa contrazione, troppo lunga, ma nella fascia di ginnastica

        l.add(new SamplingHeartbeat(104, "17:55:32"));
        l.add(new SamplingHeartbeat(101, "17:55:45"));
        l.add(new SamplingHeartbeat(99, "17:56:00"));
        l.add(new SamplingHeartbeat(98, "17:56:17"));
        l.add(new SamplingHeartbeat(93, "17:56:28"));
        l.add(new SamplingHeartbeat(89, "17:56:40"));
        l.add(new SamplingHeartbeat(84, "17:56:56"));
        l.add(new SamplingHeartbeat(79, "17:57:06"));
        l.add(new SamplingHeartbeat(78, "17:57:19"));
        l.add(new SamplingHeartbeat(null, "17:57:29"));
        l.add(new SamplingHeartbeat(83, "17:57:41"));
        l.add(new SamplingHeartbeat(84, "17:57:55"));//no contrazione

        l.add(new SamplingHeartbeat(108, "17:58:07"));
        l.add(new SamplingHeartbeat(116, "17:58:15"));
        l.add(new SamplingHeartbeat(125, "17:58:22"));
        l.add(new SamplingHeartbeat(132, "17:58:29"));
        l.add(new SamplingHeartbeat(130, "17:58:35"));
        l.add(new SamplingHeartbeat(126, "17:58:48"));
        l.add(new SamplingHeartbeat(118, "17:58:59"));
        l.add(new SamplingHeartbeat(110, "17:59:05"));
        l.add(new SamplingHeartbeat(104, "17:59:19"));//sintomi di contrazione moderata, ma nella fascia di ginnastica

        l.add(new SamplingHeartbeat(104, "17:59:32"));
        l.add(new SamplingHeartbeat(101, "17:59:45"));
        l.add(new SamplingHeartbeat(99, "18:00:00"));
        l.add(new SamplingHeartbeat(null, "18:00:17"));
        l.add(new SamplingHeartbeat(93, "18:00:28"));
        l.add(new SamplingHeartbeat(89, "18:00:40"));
        l.add(new SamplingHeartbeat(84, "18:00:56"));
        l.add(new SamplingHeartbeat(null, "18:00:06"));
        l.add(new SamplingHeartbeat(78, "18:00:19"));
        l.add(new SamplingHeartbeat(81, "18:00:29"));
        l.add(new SamplingHeartbeat(83, "18:00:41"));
        l.add(new SamplingHeartbeat(84, "18:00:55"));//no contrazione

        l.add(new SamplingHeartbeat(null, "18:01:30"));
        l.add(new SamplingHeartbeat(null, "18:02:00"));
        l.add(new SamplingHeartbeat(null, "18:02:30"));
        l.add(new SamplingHeartbeat(null, "18:03:00"));
        l.add(new SamplingHeartbeat(null, "18:03:30"));
        l.add(new SamplingHeartbeat(null, "18:04:00"));
        l.add(new SamplingHeartbeat(null, "18:04:30"));
        l.add(new SamplingHeartbeat(null, "18:05:00"));
        l.add(new SamplingHeartbeat(null, "18:05:30"));//nessun campionamento per cinque minuti

        l.add(new SamplingHeartbeat(108, "18:06:00"));
        l.add(new SamplingHeartbeat(116, "18:06:15"));
        l.add(new SamplingHeartbeat(125, "18:06:22"));
        l.add(new SamplingHeartbeat(132, "18:06:29"));
        l.add(new SamplingHeartbeat(130, "18:06:35"));
        l.add(new SamplingHeartbeat(126, "18:06:48"));
        l.add(new SamplingHeartbeat(118, "18:06:59"));
        l.add(new SamplingHeartbeat(110, "18:07:05"));
        l.add(new SamplingHeartbeat(104, "18:07:19"));//sintomi di contrazione moderata, fuori dalla fascia di ginnastica

        l.add(new SamplingHeartbeat(null, "18:07:30"));
        l.add(new SamplingHeartbeat(null, "18:08:00"));
        l.add(new SamplingHeartbeat(null, "18:09:30"));
        l.add(new SamplingHeartbeat(null, "18:09:00"));//nessun campionamento per due minuti

        l.add(new SamplingHeartbeat(107, "18:09:30"));//isolata, non valutabile

        l.add(new SamplingHeartbeat(103, "18:10:00"));
        l.add(new SamplingHeartbeat(100, "18:10:30"));
        l.add(new SamplingHeartbeat(99, "18:11:00"));
        l.add(new SamplingHeartbeat(95, "18:11:30"));
        l.add(new SamplingHeartbeat(92, "18:12:00"));
        l.add(new SamplingHeartbeat(96, "18:12:30"));//no contrazione

        l.add(new SamplingHeartbeat(108, "18:13:00"));
        l.add(new SamplingHeartbeat(116, "18:13:15"));
        l.add(new SamplingHeartbeat(125, "18:13:22"));
        l.add(new SamplingHeartbeat(132, "18:13:29"));
        l.add(new SamplingHeartbeat(null, "18:13:35"));
        l.add(new SamplingHeartbeat(126, "18:13:48"));
        l.add(new SamplingHeartbeat(118, "18:13:59"));
        l.add(new SamplingHeartbeat(110, "18:14:05"));
        l.add(new SamplingHeartbeat(106, "18:14:19"));//sintomi di contrazione moderata, fuori dalla fascia di ginnastica

        l.add(new SamplingHeartbeat(103, "18:14:30"));
        l.add(new SamplingHeartbeat(98, "18:14:41"));
        l.add(new SamplingHeartbeat(92, "18:14:53"));
        l.add(new SamplingHeartbeat(87, "18:15:00"));//no contrazione
    }
}