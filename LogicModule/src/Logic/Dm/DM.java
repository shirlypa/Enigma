package Logic.Dm;

import Logic.Agent.Agent;
import Logic.Agent.SuccessString;
import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private BlockingQueue<SuccessString> validStringQueue;
    private List<SuccessString> validStringList;
    private MachineDescriptor machineDescriptor;
    private final int k_missionsNumber;
    private Secret knownSecret;
    private int accomplishedMissions;
    private Instant startWorkInstant;
    private Map<Integer,Mission> agentCurrentMissionMap;

    public DM(hasUItoShowMissions programManager, eProccessLevel processLevel, int oneMissionSize, int agentNumber, MachineDescriptor machineDescriptor){
        this.machineDescriptor = machineDescriptor;
        this.agentNumber = agentNumber;
        this.missionSize = oneMissionSize;
        this.programManager = programManager;
        this.processLevel = processLevel;
        this.accomplishedMissions = 0;
        this.validStringList = new ArrayList<>();
        this.agentCurrentMissionMap = new HashMap<>();

        //TODO calc mWorkSize
        mWorkSize = 9999;
        agentList = new ArrayList<>();
        k_missionsNumber = (int)mWorkSize / oneMissionSize;
        toDoMissionsQueue = new ArrayBlockingQueue<>(k_missionsNumber);
        validStringQueue = new ArrayBlockingQueue<>(k_missionsNumber);
    }

    @Override
    public void run() {
        startWorkInstant = Instant.now();
        calcMissionToCreateBeforeAgentsStart();
        createAgentsList();
        setKnownSecret();
        MissionsProducerThread missionProd = new MissionsProducerThread(toDoMissionsQueue,processLevel,k_missionsNumber,missionSize,machineDescriptor,knownSecret);
        new Thread(missionProd).start();
        while (accomplishedMissions < k_missionsNumber){
            try {
                SuccessString successString = validStringQueue.take();
                synchronized (validStringList) {
                    validStringList.add(successString);
                }
                accomplishedMissions++;
                programManager.inProcessingUpdates(accomplishedMissions,validStringList.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
                //TODO NOY
            }
        }
        WorkSummery workSummery = createWorkSummery();
        programManager.dmDoneWorking(workSummery);
        stopAgentsWork();
    }

    private void stopAgentsWork() {
        //TODO NOY
    }

    private WorkSummery createWorkSummery() {
        long timeFromStart = Duration.between(startWorkInstant,Instant.now()).toMillis();
        return new WorkSummery(accomplishedMissions,mWorkSize,agentCurrentMissionMap,validStringList,timeFromStart);
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

    public void onAgentStartMission(int agentID, Mission mission){
        agentCurrentMissionMap.put(agentID,mission);
    }
}
