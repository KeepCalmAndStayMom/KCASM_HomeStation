package homestation.hospital;

import homestation.fitbit.SamplingHeartbeat;
import java.util.ArrayList;

public interface SamplingListEvaluationStrategy {
    public String contractionTypeEvaluation(ArrayList<ArrayList<SamplingHeartbeat>> evaluationList, int pregnancyWeek);
}