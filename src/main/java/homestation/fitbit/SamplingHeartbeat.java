package homestation.fitbit;

public class SamplingHeartbeat {
    public Integer heartbeat;
    public String date;

    public SamplingHeartbeat(Integer heartbeat, String date) {
        this.heartbeat = heartbeat;
        this.date = date;
    }

    @Override
    public String toString() {
        return "battito: " + heartbeat + ", data: " + date;
    }
}