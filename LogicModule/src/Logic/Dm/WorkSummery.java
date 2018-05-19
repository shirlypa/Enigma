package Logic.Dm;

import Logic.Agent.SuccessString;

import java.util.List;
import java.util.Map;

//******************************
//intance of this class will be passed to ProgramManager by the DM, when DM done all of the missions (done the work).
//******************************
//should have contain (exposed as public functions):
//the time of the work (all missions)
//number of missions in the work
//number of accomplished missions
//for each agent: the number of mission that remain and the current mission
//list of successed string. each item should have:
//      Object(String successedString, Secret secretWhenSuccessed)
public class WorkSummery {
    //TODO this class
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
        //TODO NOY
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
