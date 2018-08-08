package Logic.Dm;

//import Agent.Agent;
//import AgentDMParts.SuccessString;
import AgentDMParts.*;
import AgentDMParts.MachineComponents.Reflector;
import AgentDMParts.MachineComponents.Rotor;
import AgentDMParts.MachineDescriptor;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;

public class DM extends Thread implements Runnable {
    private String txtToDecipher;
    //private List<Agent> agentList;
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
    private int accomplishedMissions;
    private Instant startWorkInstant;
    private Map<Integer, Secret> agentCurrentMissionMap;
    private eDM_State dm_state;
    private SecretGenerator secretGenerator;
    private MissionsProducerThread missionProd;
    private long missionsNumber;
    private long workTime;
    private ServerSockets serverSockets;
    private int port;

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
        this.secretGenerator = new SecretGenerator(processLevel,machineDescriptor.getRotorsInUseCount(), machineDescriptor.getAlphabet());
        this.port = port;
        mWorkSize = calcWorkSize();
      //  agentList = new ArrayList<>();

        toDoMissionsQueue = new ArrayBlockingQueue<>(K_QUEUE_SIZE);
        validStringQueue = new ArrayBlockingQueue<>(K_QUEUE_SIZE);

        serverSockets = new ServerSockets(0,toDoMissionsQueue);
        port = serverSockets.getPort();
        serverSockets.start();
        //TODO just for debbugging
        System.out.println(port);
    }
    public DM()
    {
        serverSockets = new ServerSockets(0,toDoMissionsQueue);
        port = serverSockets.getPort();
        serverSockets.start();
    }

    public int getPort(){return this.port;}

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
        boolean first = true;
        try {
            while(serverSockets.getAgents().size() <2){
                if(first) {
                    System.out.println("need to run agent!");
                    first=false;
                }
            }
            System.out.println("2 Agent connected");
            createAgentsList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        missionProd = new MissionsProducerThread(serverSockets.getAgents(),this,toDoMissionsQueue,processLevel,missionSize,machineDescriptor, mWorkSize);
        missionProd.setSecretGenerator(secretGenerator);

        //missionProd.setAgentList(agentList);
        missionProd.setName("MissionProducer-Thread");
        missionProd.setMissionsToCraateBeforeStartAgents(missionToCreateBeforeStartAgents);
        missionProd.start();
        try {
            startAgents();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Start listening to accomplishedMissions
        while (!missionProd.getFinish()|| missionsNumber > accomplishedMissions){
            if (this.isInterrupted()){
                try {
                    handleInterrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SuccessString successString = validStringQueue.poll();
            if (successString != null) {
                synchronized (validStringList) {
                    validStringList.add(successString);
                }
            }
        }
        System.out.println("\n\n DM END WORK! Select Pause command to see updated information and then stop to go back to Main Menu");
        this.workTime = Duration.between(startWorkInstant,Instant.now()).toMillis();
        this.setDm_state(eDM_State.PAUSE);
        synchronized (this) {
            while (this.getDm_state().equals(eDM_State.PAUSE))
                try {
                    this.wait();
                } catch (InterruptedException e1) {
                    try {
                        handleInterrupt();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
        try {
            interruptAllAgents();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void startAgents() throws IOException {
        for (ComManager agent:serverSockets.getAgents())
        {
            agent.sendMsg(new Data("start", Data.eDataType.START_AGENT));
        }
    }
    private void handleInterrupt() throws IOException {
        interruptAllAgents();
        if (this.getDm_state().equals(eDM_State.DONE)){
            return;
        }
        synchronized (this) {
            while (!this.dm_state.equals(eDM_State.RUNNING)) {
                try {
                    eDM_State currentState = getDm_state();
                    this.wait();
                } catch (InterruptedException e1) {
                    handleInterrupt();
                }
            }
        }
    }

    //DOTO how to interupt!!
    private void interruptAllAgents() throws IOException {
        for (ComManager agent : serverSockets.getAgents()) {
            agent.sendMsg(new Data("", Data.eDataType.INTERRUPT));
        }
        if (missionProd.getState().equals(State.RUNNABLE)) {
            missionProd.interrupt();
        }
    }

    public WorkSummery createWorkSummery() {
        long timeFromStart;
        if (workTime != 0) { //the work was ended
            timeFromStart = workTime;
        } else {
            timeFromStart = Duration.between(startWorkInstant, Instant.now()).toMillis();
        }
        return new WorkSummery(accomplishedMissions,missionsNumber,agentCurrentMissionMap,validStringList,timeFromStart);
    }

    private void createAgentsList() throws IOException {
        for (ComManager agent : serverSockets.getAgents()) {

            //TODO pass the MACHINE AND THE QUEUE?
            agent.sendMsg(new Data(machineDescriptor,Data.eDataType.MACHINE));
            System.out.println("Send Machine");
            agent.sendMsg(new Data(txtToDecipher, Data.eDataType.SOURCE));
            System.out.println("Send SourceString");
            agent.sendMsg(new Data(machineDescriptor.getAlphabet(), Data.eDataType.ALPHABET));
            System.out.println("Send Alphabet");
            agent.sendMsg(new Data(machineDescriptor.getDictionary(), Data.eDataType.DICTIONERY));
            System.out.println("Send Dictionery");

            //newAgent.setName("Agent-" + (i+1) + "-Thread");
            //agentList.add(newAgent);
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

    //worksize/onemissionsize/10
    private void calcMissionToCreateBeforeAgentsStart() {
        missionToCreateBeforeStartAgents = max(Math.round(mWorkSize/missionSize/10),K_QUEUE_SIZE);
    }

    public long getWorkSize() {
        return mWorkSize;
    }

    public void onAgentStartMission(int agentID, Mission mission){
        agentCurrentMissionMap.put(agentID,mission.getInitialSecret());
    }

    public synchronized eDM_State getDm_state() {
        return dm_state;
    }

    public synchronized void setDm_state(eDM_State dm_state) {
        this.dm_state = dm_state;
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
        this.missionsNumber = mWorkSize / missionSize;
    }

    public void setAgentNumber(int agentNumber) {this.agentNumber = agentNumber; }

    public synchronized void accomplishedMissionsPlusPlus() {
        accomplishedMissions++;
    }

    public synchronized void updateAgentCurrentMission(int agentID, Secret currentInitialSecret){
        //TODO update (put may not success because it already exist)
        agentCurrentMissionMap.put(agentID,currentInitialSecret);
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

    public void setMissionsNumber(int missionsNumber) {
        this.missionsNumber = missionsNumber;
    }

    public int gerQueueSize(){return K_QUEUE_SIZE;}

    public void stopAgents(boolean isWinner) throws IOException {
        String msg;
        if (isWinner){
            msg = "We Won";
        }
        else {
            msg = "We Lost";
        }
        for (ComManager agent:serverSockets.getAgents())
        {
            agent.sendMsg(new Data(msg, Data.eDataType.CLOSE));
        }
    }

    public List<Integer> getAgentInfo() {
        List<Integer> AgentInfo = new ArrayList<>();
        for (ComManager agent:serverSockets.getAgents())
        {
            AgentInfo.add(agent.getSuccsesString());
        }
        return AgentInfo;
    }
}
