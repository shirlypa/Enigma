package Agent;
import AgentDMParts.*;
//import Logic.Dm.DM;
import AgentDMParts.MachineComponents.Reflector;
import AgentDMParts.MachineComponents.Rotor;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
//import pukteam.enigma.component.rotor.Rotor;
import pukteam.enigma.factory.EnigmaComponentFactory;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class  Agent extends Thread implements Runnable {
    private BlockingQueue<Mission> toDoMissionsQueue;
    private BlockingQueue<SuccessString> accomplishedMissionsQueue;
    private EnigmaMachine machineInst;
    private String source;
    private Dictionary dictionary;
    private String alphabet;
    private int agentID;
    private ComManager comManager;

    //private DM dm;
    public Agent(BlockingQueue<Mission> toDoMissionsQueue, BlockingQueue<SuccessString> accomplishedMissionsQueue, EnigmaMachine machine, String source, Dictionary dictionary,String alphabet, int agentID)
    {
        this.toDoMissionsQueue=toDoMissionsQueue;
        this.accomplishedMissionsQueue =accomplishedMissionsQueue;
        this.machineInst=machine;
        this.source = source;
        this.dictionary = dictionary;
        this.alphabet = alphabet;
        this.agentID = agentID;
        //this.dm = dm;
    }
    public Agent(BlockingQueue<Mission> missionToDo, BlockingQueue<SuccessString> accomplishedMissionsQueue,ComManager comManager)
    {
        this.toDoMissionsQueue = missionToDo;
        this.accomplishedMissionsQueue = accomplishedMissionsQueue;
        this.comManager = comManager;
    }


    private EnigmaMachine createMachineInstance(MachineDescriptor machineDescriptor) {
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

    public void setDictionary(Dictionary dic)
    {
        this.dictionary=dic;
    }
    @Override
    public void run() {
        Mission mission;
        while (true)
        {
            try {
                //notify the DM the mission was taken
                //ask the DM for a mission
                comManager.sendMessage(new Data("", Data.eDataType.MISSION_TODO));
                mission = toDoMissionsQueue.take();
                runMission(mission);
               // dm.accomplishedMissionsPlusPlus();
            }
            catch(InterruptedException e)
            {
                //handleInterrupt();
            }
        }
    }


//    private void handleInterrupt() {
//        if (dm.getDm_state().equals(eDM_State.DONE)){
//            return;
//        }
//        synchronized (dm) {
//            while (!dm.getDm_state().equals(eDM_State.RUNNING)) {
//                try {
//                    dm.wait();
//                } catch (InterruptedException e1) {
//                    handleInterrupt();
//                }
//            }
//        }
//    }

    private void runMission(Mission mission) throws InterruptedException {
        String result;
        SuccessString successString;
        Secret currentSecret = mission.getInitialSecret();
        for(int i=0;i<mission.getMissionSize();i++)
        {
            currentSecret.createEnigmaMachineSecret(machineInst);
            result = machineInst.process(source);
            if (dictionary.isExistsInDictionary(result))
            {
                String cleanResult = dictionary.removeSpecialChars(result);
                successString = new SuccessString(cleanResult,currentSecret.cloneSecret(),agentID);
                accomplishedMissionsQueue.put(successString);
                System.out.println(successString.getSucessString());
            }
            currentSecret.advanceRotors(alphabet);
        }
    }


    public void setMachine(MachineDescriptor machine) {
        this.machineInst = createMachineInstance(machine);
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setAlphaBet(String alphaBet) {
        this.alphabet = alphaBet;
    }

    public void setMissionQueue(BlockingQueue<Mission> missionQueue) {
        this.toDoMissionsQueue = missionQueue;
    }

    public void printMsg(String msg) {
        System.out.println(msg);
    }
}
