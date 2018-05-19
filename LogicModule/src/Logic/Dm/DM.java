package Logic.Dm;

import Logic.Agent.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DM implements Runnable {
    private List<Agent> agentList;
    private long mWorkSize;
    private int agentNumber;
    private int missionSize;
    private hasUItoShowMissions programManager;
    private int missionToCreateBeforeStartAgents;
    private BlockingQueue<Mission> toDoMissionsQueue;
    private BlockingQueue<Mission> accomplishedMissionsQueue;
    private final int k_missionsNumber;

    public DM (hasUItoShowMissions programManager, eProccessLevel processLevel, int oneMissionSize, int agentNumber){
        //TODO calc mWorkSize
        mWorkSize = 9999;
        this.agentNumber = agentNumber;
        this.missionSize = oneMissionSize;
        this.programManager = programManager;
        agentList = new ArrayList<>();
        k_missionsNumber = (int)mWorkSize / oneMissionSize;
        toDoMissionsQueue = new ArrayBlockingQueue<>(k_missionsNumber);
        accomplishedMissionsQueue = new ArrayBlockingQueue<>(k_missionsNumber);
    }

    @Override
    public void run() {
        calcMissionToCreateBeforeAgentsStart();
        createAgentsList();
    }

    private void createAgentsList() {
        for (int i = 0; i < agentNumber; i++) {
             agentList.add(new Agent());
        }
    }

    private void calcMissionToCreateBeforeAgentsStart() {
    }

    public long getWorkSize() {
        return mWorkSize;
    }
}
