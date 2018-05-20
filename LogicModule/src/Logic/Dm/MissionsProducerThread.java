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
    private long workSize;

    public MissionsProducerThread(BlockingQueue<Mission> missionsQueue, eProccessLevel proccessLevel, int mMissionSize, MachineDescriptor machineDescriptor, Secret knownSecret) {
        this.missionsQueue = missionsQueue;
        this.proccessLevel = proccessLevel;
        this.missionsNumber = 0;
        this.mMissionSize = mMissionSize;
        this.machineDescriptor = machineDescriptor;
        this.knownSecret = knownSecret;
    }

    @Override
    public void run() {
        Mission mission;
        String alphabet = machineDescriptor.getAlphabet();
        boolean codeWasReset;
        Secret currentMissionInitialSecret, advancedSecret;
        int currentMissionSize = 1;
        for (long i = 0; i < workSize; i++,currentMissionSize++){
            codeWasReset = advancedSecret.advanceRotors(alphabet);
            if (codeWasReset || currentMissionSize == mMissionSize) {
                synchronized (this) {
                    mission = new Mission(missionsNumber++, currentMissionInitialSecret, currentMissionSize);
                }
                currentMissionSize = 1;
                if (codeWasReset) {
                    currentMissionInitialSecret = getNextSecretByProccessLevel();
                    advancedSecret = currentMissionInitialSecret.cloneSecret();
                } else {
                    currentMissionInitialSecret = advancedSecret.cloneSecret();
                }
                //put mission in missionQueue
                try {
                    missionsQueue.put(mission);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //TODO NOY
                }
            }
        }
    }

    private Secret getNextSecretByProccessLevel(){

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
