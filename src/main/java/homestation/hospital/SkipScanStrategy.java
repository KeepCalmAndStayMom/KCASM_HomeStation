package homestation.hospital;

import homestation.fitbit.SamplingHeartbeat;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class SkipScanStrategy implements SamplingListScanStrategy {
    @Override
    public ArrayList<ArrayList<SamplingHeartbeat>> createEvaluationList(ArrayList<SamplingHeartbeat> l) {
        if (l == null || l.size() == 0)
            return null;

        SamplingHeartbeat previousSampling = null;
        ArrayList<ArrayList<SamplingHeartbeat>> evaluationList = new ArrayList<>();
        ArrayList<SamplingHeartbeat> lTmp = new ArrayList<>();

        for (SamplingHeartbeat c : l) {
            if (c.heartbeat == null)
                continue;

            if (previousSampling == null) {
                previousSampling = c;
                lTmp.add(c);
                continue;
            }

            if ((previousSampling.heartbeat > HospitalConstants.MINIMUM_THRESHOLD && c.heartbeat <= HospitalConstants.MINIMUM_THRESHOLD) || (previousSampling.heartbeat <= HospitalConstants.MINIMUM_THRESHOLD && c.heartbeat > HospitalConstants.MINIMUM_THRESHOLD)) {
                evaluationList.add(new ArrayList<>(lTmp));
                lTmp.clear();
                lTmp.add(c);
            }
            else {
                if ((int) ChronoUnit.SECONDS.between(LocalTime.parse(previousSampling.time, HospitalConstants.FORMATTER), LocalTime.parse(c.time, HospitalConstants.FORMATTER)) > HospitalConstants.MAXIMUM_SKIP_BETWEEN_SAMPLINGS) {
                    evaluationList.add(new ArrayList<>(lTmp));
                    lTmp.clear();
                }
                lTmp.add(c);
            }
            previousSampling = c;
        }

        evaluationList.add(lTmp);

        return evaluationList;
    }
}
