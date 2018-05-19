package Logic.Dm;


import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;

import java.util.concurrent.BlockingDeque;

public class MissionsProducerThread implements Runnable {
    private BlockingDeque<Mission> missionsQueue;
    private eProccessLevel proccessLevel;
    private int missionsNumber;
    private int mMissionSize;
    private MachineDescriptor machineDescriptor;
    private Secret knownSecret;
    @Override

    public void run() {
        Mission mission;
        for (int i = 0; i < missionsNumber; i++) {
            mission = createOneMission();
            missionsQueue.add(mission);
        }
    }

    private Mission createOneMission() {
        //TODO

        return null;
    }

}
