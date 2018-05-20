package Logic.Dm;

import Logic.Agent.Agent;
import Logic.Agent.SuccessString;
import Logic.MachineDescriptor.MachineComponents.Reflector;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.Math.pow;

public class DM extends Thread implements Runnable {
    private String txtToDecipher;
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
    private SecretGenerator secretGenerator;

    public DM(String txtToDecipher, hasUItoShowMissions programManager, eProccessLevel processLevel, int agentNumber, MachineDescriptor machineDescriptor){
        this.txtToDecipher = txtToDecipher;
        this.machineDescriptor = machineDescriptor;
        this.agentNumber = agentNumber;
        this.programManager = programManager;
        this.processLevel = processLevel;
        this.accomplishedMissions = 0;
        setDm_state(eDM_State.RUNNING);
        this.validStringList = new ArrayList<>();
        this.agentCurrentMissionMap = new HashMap<>();
        this.secretGenerator = new SecretGenerator(processLevel,machineDescriptor.getRotorsInUseCount());
        //TODO calc mWorkSize
        mWorkSize = calcWorkSize();
        agentList = new ArrayList<>();

        toDoMissionsQueue = new ArrayBlockingQueue<>(K_QUEUE_SIZE);
        validStringQueue = new ArrayBlockingQueue<>(K_QUEUE_SIZE);
    }

    private long calcWorkSize() {
        long result = (long)pow(machineDescriptor.getAlphabet().length(),machineDescriptor.getRotorsInUseCount());
        if (!processLevel.equals(eProccessLevel.EASY)){
            result *= machineDescriptor.getAvaliableReflector().size();
            if (!processLevel.equals(eProccessLevel.MEDIUM)) {
                for (int i = machineDescriptor.getRotorsInUseCount(); i > 0; i--) {
                    result *= i;
                }
                if (!processLevel.equals(eProccessLevel.HARD)){
                    for(int i = 0; i < machineDescriptor.getRotorsInUseCount(); i++){
                        result *= (machineDescriptor.getAvaliableRotors().size() - i);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void run() {
        startWorkInstant = Instant.now();
        calcMissionToCreateBeforeAgentsStart();
        createAgentsList();
        MissionsProducerThread missionProd = new MissionsProducerThread(this,toDoMissionsQueue,processLevel,missionSize,machineDescriptor,knownSecret);
        new Thread(missionProd).start();
        //Start listening to accomplishedMissions
        while (accomplishedMissions < missionProd.getMissionsNumber()){
            try {
                SuccessString successString = validStringQueue.take();
                synchronized (validStringList) {
                    validStringList.add(successString);
                }
                accomplishedMissions++;
            } catch (InterruptedException e) {
                interruptAllAgents();
                if (this.dm_state.equals(eDM_State.DONE)){
                    return;
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
        this.setDm_state(eDM_State.DONE);
        interruptAllAgents();
        System.out.println("\n\n DM END WORK! Select Pause command to see updated information and then stop to go back to Main Menu");
    }

    private void interruptAllAgents() {
        for (Agent agent : agentList) {
            agent.interrupt();
        }
    }

    public WorkSummery createWorkSummery() {
        long timeFromStart = Duration.between(startWorkInstant,Instant.now()).toMillis();
        return new WorkSummery(accomplishedMissions,mWorkSize,agentCurrentMissionMap,validStringList,timeFromStart);
    }

    private void createAgentsList() {
        for (int i = 0; i < agentNumber; i++) {
             agentList.add(new Agent(toDoMissionsQueue,validStringQueue,createMachineInstance()),machineDescriptor.getDictionary());
        }
    }

    private EnigmaMachine createMachineInstance() {
        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(machineDescriptor.getRotorsInUseCount(),machineDescriptor.getAlphabet());
        Map<Integer,Rotor> availableRotors = machineDescriptor.getAvaliableRotors();
        Map<Integer,Reflector> availableReflectors = machineDescriptor.getAvaliableReflector();
        for (Rotor r:availableRotors.values()) {
            machineBuilder.defineRotor(r.getID(),r.getSource(),r.getDest(),r.getNotch());
        }

        for (Reflector r:availableReflectors.values()) {
            machineBuilder.defineReflector(r.getID(),r.getSource(),r.getDest());
        }
        return machineBuilder.create();
    }

    private void calcMissionToCreateBeforeAgentsStart() {
    }

    public long getWorkSize() {
        return mWorkSize;
    }

    public void onAgentStartMission(int agentID, Mission mission){
        agentCurrentMissionMap.put(agentID,mission);
    }

    public synchronized eDM_State getDm_state() {
        return dm_state;
    }

    public synchronized void setDm_state(eDM_State dm_state) {
        this.dm_state = dm_state;
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }

    public static void accomplishedMissionsPlusPlus() {
    }

    public void setKnown_RotorsIDs(int[] rotorsIDSelectArr) {
        secretGenerator.setCurrentRotorsInUse(rotorsIDSelectArr);
    }

    public void setKnown_RotorsOrder(boolean b) {
        secretGenerator.setRotorsOrdered(b);
    }

    public void setKnown_Reflector(int secretReflectorInUse) {
        secretGenerator.setCurrentReflectorInUse(secretReflectorInUse);
    }
}
