package Logic.Dm;

import Logic.Agent.SuccessString;
import Logic.MachineDescriptor.MachineComponents.Secret;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WorkSummery {
    private int accomplishMissions;
    private long missionsNumber;
    Map<Integer,Secret> agentCurrentMissionMap;
    private List<SuccessString> successStrings;
    private String timeFromStart; //mm:ss or hh:mm:ss if there is hours

    public WorkSummery(int accomplishMissions, long missionsNumber, Map<Integer, Secret> agentCurrentMissionMap, List<SuccessString> successStrings, long timeFromStart) {
        this.accomplishMissions = accomplishMissions;
        this.missionsNumber = missionsNumber;
        this.agentCurrentMissionMap = agentCurrentMissionMap;
        this.successStrings = successStrings;
        this.timeFromStart = timeToStr(timeFromStart);
    }

    private String timeToStr(long timeFromStart) {
        long seconds, minutes, hours;
        hours = TimeUnit.MILLISECONDS.toHours(timeFromStart);
        minutes = TimeUnit.MILLISECONDS.toMinutes(timeFromStart - TimeUnit.HOURS.toMillis(hours));
        seconds = TimeUnit.MILLISECONDS.toSeconds(timeFromStart- TimeUnit.HOURS.toMillis(hours)- TimeUnit.MINUTES.toMillis(minutes));
        return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }

    public int getAccomplishMissions() {
        return accomplishMissions;
    }


    public Map<Integer, Secret> getAgentCurrentMissionMap() {
        return agentCurrentMissionMap;
    }

    public List<SuccessString> getSuccessStrings() {
        return successStrings;
    }

    public String getTimeFromStart() {
        return timeFromStart;
    }

    public String getPrecent() {
        return ((float)accomplishMissions / (float)missionsNumber * 100) + "%";
    }
}
