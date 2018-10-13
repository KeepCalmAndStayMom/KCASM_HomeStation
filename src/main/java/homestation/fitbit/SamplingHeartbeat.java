package homestation.fitbit;

public class SamplingHeartbeat implements Comparable<SamplingHeartbeat> {
    public Integer heartbeat;
    public String time;

    public SamplingHeartbeat(Integer heartbeat, String time) {
        this.heartbeat = heartbeat;
        this.time = time;
    }

    @Override
    public String toString() {
        return "(battito: " + heartbeat + ", ora: " + time + ")";
    }

    @Override
    public int compareTo(SamplingHeartbeat other) {
            return time.compareTo(other.time);
    }
}