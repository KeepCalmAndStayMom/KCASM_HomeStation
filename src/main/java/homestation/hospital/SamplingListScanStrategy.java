package homestation.hospital;

import homestation.fitbit.SamplingHeartbeat;

import java.util.ArrayList;

public interface SamplingListScanStrategy {
    public ArrayList<ArrayList<SamplingHeartbeat>> createEvaluationList(ArrayList<SamplingHeartbeat> l);
}