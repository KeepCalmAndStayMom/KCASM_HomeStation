package homestation.zway;

import de.fh_zwickau.informatik.sensor.IZWayApi;
import de.fh_zwickau.informatik.sensor.ZWayApiHttp;
import de.fh_zwickau.informatik.sensor.model.devices.Device;
import homestation.HomestationSettings;

public class ZWaySensor {

    private IZWayApi zwayApi = new ZWayApiHttp(HomestationSettings.IPADRESS_ZWAY, 8083, "http", HomestationSettings.USERNAME, HomestationSettings.PASSWORD, 0, false, new ZWaySimpleCallback());

    private int nodeId;
    private String movement;
    private double temperature;
    private double luminescence;
    private double humidity;

    public ZWaySensor(int nodeId) {
        this.nodeId = nodeId;
        sensorsManagement();
    }

    public synchronized void sensorsManagement() {

        Device temp = zwayApi.getDevice("ZWayVDev_zway_" + nodeId + "-0-49-1");
        Device lum = zwayApi.getDevice("ZWayVDev_zway_" + nodeId + "-0-49-3");
        Device hum = zwayApi.getDevice("ZWayVDev_zway_" + nodeId + "-0-49-5");
        Device mov = zwayApi.getDevice("ZWayVDev_zway_" + nodeId + "-0-48-1");
        temp.update();
        lum.update();
        hum.update();
        mov.update();

        temperature = Double.parseDouble(temp.getMetrics().getLevel());
        luminescence = Double.parseDouble(lum.getMetrics().getLevel());
        humidity = Double.parseDouble(hum.getMetrics().getLevel());
        movement = mov.getMetrics().getLevel();
    }

    public synchronized String toJson() {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"movement\": ").append(movement).append(",");
        builder.append("\"temperature\": ").append(temperature).append(",");
        builder.append("\"luminescence\": ").append(luminescence).append(",");
        builder.append("\"humidity\": ").append(humidity).append("}");

        return builder.toString();
    }

    public synchronized String getMovement() { return movement; }

    public synchronized double getLuminescence() {
        return luminescence;
    }

    public synchronized double getTemperature() {
        return temperature;
    }

    public synchronized double getHumidity() {
        return humidity;
    }
}