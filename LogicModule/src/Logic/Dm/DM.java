package Logic.Dm;

import Logic.Agent.Agent;
import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DM implements Runnable {
    private List<Agent> agentList;
    private long mWorkSize;
    private int agentNumber;
    private eProccessLevel processLevel;
    private int missionSize;
    private hasUItoShowMissions programManager;
    private int missionToCreateBeforeStartAgents;
    private BlockingQueue<Mission> toDoMissionsQueue;
    private BlockingQueue<Mission> agentsToDmQueue;
    private MachineDescriptor machineDescriptor;
    private final int k_missionsNumber;
    private Secret knownSecret;
    private int accomplishedMissions;

    public DM(hasUItoShowMissions programManager, eProccessLevel processLevel, int oneMissionSize, int agentNumber, MachineDescriptor machineDescriptor){
        this.machineDescriptor = machineDescriptor;
        this.agentNumber = agentNumber;
        this.missionSize = oneMissionSize;
        this.programManager = programManager;
        this.processLevel = processLevel;
        this.accomplishedMissions = 0;
        //TODO calc mWorkSize
        mWorkSize = 9999;
        agentList = new ArrayList<>();
        k_missionsNumber = (int)mWorkSize / oneMissionSize;
        toDoMissionsQueue = new ArrayBlockingQueue<>(k_missionsNumber);
        agentsToDmQueue = new ArrayBlockingQueue<>(k_missionsNumber);
    }

    @Override
    public void run() {
        //TODO Start Timer
        calcMissionToCreateBeforeAgentsStart();
        createAgentsList();
        setKnownSecret();
        MissionsProducerThread missionProd = new MissionsProducerThread(toDoMissionsQueue,processLevel,k_missionsNumber,missionSize,machineDescriptor,knownSecret);
        new Thread(missionProd).start();
        while (accomplishedMissions < k_missionsNumber){
            agentsToDmQueue.
        }


        //TODO stop timer
    }

    private void setKnownSecret() {
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
