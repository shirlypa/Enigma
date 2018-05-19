package Logic.Agent;
import Logic.Dm.Mission;
import Logic.MachineDescriptor.MachineComponents.Dictionary;
import Logic.MachineDescriptor.MachineComponents.Secret;
import pukteam.enigma.component.machine.api.EnigmaMachine;

import java.util.concurrent.BlockingQueue;

public class  Agent implements Runnable {
    private BlockingQueue<Mission> toDoMissionsQueue;
    private BlockingQueue<Mission> accomplishedMissionsQueue;
    private EnigmaMachine machineInst;
    private String source;
    private Dictionary dictionary;
    public Agent(BlockingQueue<Mission> toDoMissionsQueue, BlockingQueue<Mission> accomplishedMissionsQueue, EnigmaMachine machine, String source, Dictionary dictionary)
    {
        this.toDoMissionsQueue=toDoMissionsQueue;
        this.accomplishedMissionsQueue =accomplishedMissionsQueue;
        this.machineInst=machine;
        this.source = source;
        this.dictionary = dictionary;
    }
    @Override
    public void run() {
        Mission mission;
        while (true)
        {
            try {
                mission = toDoMissionsQueue.take();
                runMission(mission);
            }
            catch(InterruptedException e)
            {
                // TODO check if this is Exit or Pause
            }
        }
    }

    private void runMission(Mission mission) {
        String result;
        Secret currentSecret = mission.getInitialSecret();
        for(int i=0;i<mission.getMissionSize();i++)
        {
            currentSecret.createEnigmaMachineSecret(machineInst);
            result = machineInst.process(source);
            if (dictionary.isExistsInDictionary(result))
            {
                //TODO put the string in the sec queue
            }
            //currentSecret++
            //currentSecret= getNextSecret(currentSecret);
        }
    }



}
