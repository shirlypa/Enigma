package Logic.Dm;

import Logic.Agent.Agent;

import java.util.List;

public class DM implements Runnable {
    List<Agent> agentList;
    long mWorkSize;
    int agentNumber;
    int missionSize;
    hasUItoShowMissions programManager;


    public DM (hasUItoShowMissions programManager, eProccessLevel processLevel, int oneMissionSize, int agentNumber){
        //TODO calc mWorkSize
        mWorkSize = 9999;
        this.agentNumber = agentNumber;
        this.missionSize = oneMissionSize;
        this.programManager = programManager;
    }

    @Override
    public void run() {

    }
}
