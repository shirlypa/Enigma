package Logic.Agent;
import Logic.Dm.DM;
import Logic.Dm.Mission;
import Logic.Dm.eDM_State;
import Logic.MachineDescriptor.MachineComponents.Dictionary;
import Logic.MachineDescriptor.MachineComponents.Secret;
import pukteam.enigma.component.machine.api.EnigmaMachine;

import java.util.concurrent.BlockingQueue;

public class  Agent extends Thread implements Runnable {
    private BlockingQueue<Mission> toDoMissionsQueue;
    private BlockingQueue<SuccessString> accomplishedMissionsQueue;
    private EnigmaMachine machineInst;
    private String source;
    private Dictionary dictionary;
    private String alphabet;
    private int agentID;
    private DM dm;
    public Agent(BlockingQueue<Mission> toDoMissionsQueue, BlockingQueue<SuccessString> accomplishedMissionsQueue, EnigmaMachine machine, String source, Dictionary dictionary,String alphabet, int agentID, DM dm)
    {
        this.toDoMissionsQueue=toDoMissionsQueue;
        this.accomplishedMissionsQueue =accomplishedMissionsQueue;
        this.machineInst=machine;
        this.source = source;
        this.dictionary = dictionary;
        this.alphabet = alphabet;
        this.agentID = agentID;
        this.dm = dm;
    }
    @Override
    public void run() {
        Mission mission;
        while (true)
        {
            try {
                //notify the DM the mission was taken
                mission = toDoMissionsQueue.take();
                runMission(mission);
                dm.accomplishedMissionsPlusPlus();
            }
            catch(InterruptedException e)
            {
                handleInterrupt();
            }
        }
    }

    private void handleInterrupt() {
        if (dm.getDm_state().equals(eDM_State.DONE)){
            return;
        }
        synchronized (dm) {
            while (!dm.getDm_state().equals(eDM_State.RUNNING)) {
                try {
                    dm.wait();
                    System.out.println("\nAgent resumed!!!!!!\n");
                } catch (InterruptedException e1) {
                    handleInterrupt();
                }
            }
        }
    }

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
            }
            currentSecret.advanceRotors(alphabet);
        }
    }



}
