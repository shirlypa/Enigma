package Logic.Dm;


import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;

import java.util.concurrent.BlockingQueue;

public class MissionsProducerThread implements Runnable {
    private BlockingQueue<Mission> missionsQueue;
    private eProccessLevel proccessLevel;
    private int missionsNumber;
    private int mMissionSize;
    private MachineDescriptor machineDescriptor;
    private Secret knownSecret;

    public MissionsProducerThread(BlockingQueue<Mission> missionsQueue, eProccessLevel proccessLevel, int missionsNumber, int mMissionSize, MachineDescriptor machineDescriptor, Secret knownSecret) {
        this.missionsQueue = missionsQueue;
        this.proccessLevel = proccessLevel;
        this.missionsNumber = missionsNumber;
        this.mMissionSize = mMissionSize;
        this.machineDescriptor = machineDescriptor;
        this.knownSecret = knownSecret;
    }

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

    public BlockingQueue<Mission> getMissionsQueue() {
        return missionsQueue;
    }

    public eProccessLevel getProccessLevel() {
        return proccessLevel;
    }

    public int getMissionsNumber() {
        return missionsNumber;
    }

    public int getmMissionSize() {
        return mMissionSize;
    }

    public MachineDescriptor getMachineDescriptor() {
        return machineDescriptor;
    }

    public Secret getKnownSecret() {
        return knownSecret;
    }
}
