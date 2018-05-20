package Logic.Dm;

import Logic.Agent.SuccessString;

import java.util.List;
import java.util.Map;

public class WorkSummery {
    private int accomplishMissions;
    private long workSize;
    Map<Integer,Mission> agentCurrentMissionMap;
    private List<SuccessString> successStrings;
    private String timeFromStart; //mm:ss or hh:mm:ss if there is hours

    public WorkSummery(int accomplishMissions, long workSize, Map<Integer, Mission> agentCurrentMissionMap, List<SuccessString> successStrings, long timeFromStart) {
        this.accomplishMissions = accomplishMissions;
        this.workSize = workSize;
        this.agentCurrentMissionMap = agentCurrentMissionMap;
        this.successStrings = successStrings;
        this.timeFromStart = timeToStr(timeFromStart);
    }

    private String timeToStr(long timeFromStart) {
        //TODO
        return null;
    }

    public int getAccomplishMissions() {
        return accomplishMissions;
    }

    public long getWorkSize() {
        return workSize;
    }

    public Map<Integer, Mission> getAgentCurrentMissionMap() {
        return agentCurrentMissionMap;
    }

    public List<SuccessString> getSuccessStrings() {
        return successStrings;
    }

    public String getTimeFromStart() {
        return timeFromStart;
    }
}
