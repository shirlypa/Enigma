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

public class DM extends Thread implements Runnable {
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
    private final int K_QUEUE_SIZE = 100;
    private Secret knownSecret;
    private int accomplishedMissions;
    private Instant startWorkInstant;
    private Map<Integer,Mission> agentCurrentMissionMap;
    private eDM_State dm_state;

    public DM(hasUItoShowMissions programManager, eProccessLevel processLevel, int agentNumber, MachineDescriptor machineDescriptor){
        this.machineDescriptor = machineDescriptor;
        this.agentNumber = agentNumber;
        this.programManager = programManager;
        this.processLevel = processLevel;
        this.accomplishedMissions = 0;
        this.dm_state = eDM_State.RUNNING;
        this.validStringList = new ArrayList<>();
        this.agentCurrentMissionMap = new HashMap<>();
        //TODO calc mWorkSize
        mWorkSize = 9999;
        agentList = new ArrayList<>();

        toDoMissionsQueue = new ArrayBlockingQueue<>(K_QUEUE_SIZE);
        validStringQueue = new ArrayBlockingQueue<>(K_QUEUE_SIZE);
    }

    @Override
    public void run() {
        startWorkInstant = Instant.now();
        calcMissionToCreateBeforeAgentsStart();
        createAgentsList();
        setKnownSecret();
        MissionsProducerThread missionProd = new MissionsProducerThread(toDoMissionsQueue,processLevel,missionSize,machineDescriptor,knownSecret);
        new Thread(missionProd).start();
        //Start listening to accomplishedMissions
        while (accomplishedMissions < missionsNumber){
            try {
                SuccessString successString = validStringQueue.take();
                synchronized (validStringList) {
                    validStringList.add(successString);
                }
                accomplishedMissions++;
                programManager.inProcessingUpdates(accomplishedMissions,validStringList.size());
            } catch (InterruptedException e) {
                interruptAllAgents();
                if (this.dm_state.equals(eDM_State.DONE)){
                    programManager.dmDoneWorking(createWorkSummery());
                }
                while (!this.dm_state.equals(eDM_State.RUNNING)){
                    try {
                        dm_state.wait();
                    } catch (InterruptedException e1) {
                        throw new RuntimeException("Erro: DM got interrupt while wait for resume");
                    }
                }
            }
        }
        dm_state = eDM_State.DONE;
        interruptAllAgents();
        programManager.dmDoneWorking(createWorkSummery());
    }

    private void interruptAllAgents() {
        for (Agent agent : agentList) {
            agent.interrupt();
        }
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

    public eDM_State getDm_state() {
        return dm_state;
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }
}
