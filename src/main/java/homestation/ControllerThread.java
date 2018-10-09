package homestation;

import homestation.fitbit.FitbitObject;
import homestation.hue.HueObject;
import homestation.hue.SettingsHue;
import homestation.zway.ZWaySensor;
import java.time.LocalDate;
import java.util.Calendar;

public class ControllerThread extends Thread {

    private HueObject obj;
    private ZWaySensor zway;
    private FitbitObject fitbit;

    ControllerThread(HueObject obj, ZWaySensor zway, FitbitObject fitbit) {
        this.obj = obj;
        this.zway = zway;
        this.fitbit = fitbit;
    }

    @Override
    public void run() {
        while (true) {
            updateDevices();
            controlHeart();
            controlMovement();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateDevices() {
        zway.sensorsManagement();
    }

    private void controlHeart() {
        Integer avg_heartbreats = fitbit.heartRate.getAvgHeartbeats();

        if(avg_heartbreats != null) {
            if (avg_heartbreats >= 110 && avg_heartbreats < 150)
                obj.chromotherapySoft();
            if (avg_heartbreats >= 150)
                obj.chromotherapyHard();
        }
    }

    private void controlMovement() {
        String mov = zway.getMovement();

        if(mov.equals("off"))
            obj.switchOffHues();
    }
}
