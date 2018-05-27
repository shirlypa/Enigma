package Logic.Dm;


import Logic.Agent.Agent;
import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MissionsProducerThread extends Thread implements Runnable {
    private BlockingQueue<Mission> missionsQueue;
    private eProccessLevel proccessLevel;
    private int missionsNumber;
    private int mMissionSize;
    private MachineDescriptor machineDescriptor;
    private long workSize;
    private SecretGenerator secretGenerator;
    private DM mDM;
    private List<Agent> agentList;
    private int missionsToCraateBeforeStartAgents;
    private boolean finish = false;

    public MissionsProducerThread(DM dm, BlockingQueue<Mission> missionsQueue, eProccessLevel proccessLevel, int mMissionSize, MachineDescriptor machineDescriptor, long workSize) {
        this.missionsQueue = missionsQueue;
        this.proccessLevel = proccessLevel;
        this.workSize = workSize;
        this.missionsNumber = 0;
        this.mMissionSize = mMissionSize;
        this.machineDescriptor = machineDescriptor;
        this.mDM = dm;
    }

    @Override
    public void run() {
        Mission mission;
        String alphabet = machineDescriptor.getAlphabet();
        boolean codeWasReset;
        Secret currentMissionInitialSecret = secretGenerator.getInitialSecret();
        Secret advancedSecret = currentMissionInitialSecret.cloneSecret();

        int currentMissionSize = 1;
        for (long i = 1; i < workSize; i++,currentMissionSize++){
            if (i == missionsToCraateBeforeStartAgents){
                startAgents();
            }
            codeWasReset = advancedSecret.advanceRotors(alphabet);
            if (codeWasReset || currentMissionSize == mMissionSize || i == workSize) {
                synchronized (this) {
                    mission = new Mission(missionsNumber++, currentMissionInitialSecret, currentMissionSize);
                }
                currentMissionSize = 1;
                if (codeWasReset && i != workSize) {
                    currentMissionInitialSecret = secretGenerator.advanceRotorsAndReflectorByLevel();
                    advancedSecret = currentMissionInitialSecret.cloneSecret();
                } else {
                    i++;
                    advancedSecret.advanceRotors(alphabet);
                    currentMissionInitialSecret = advancedSecret.cloneSecret();
                }
                //put mission in missionQueue
                try {
                    missionsQueue.put(mission);
                    //System.out.println(this.getName() + " >> New mission: " + mission);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    if (mDM.getDm_state().equals(eDM_State.DONE)){
                        return;
                    }
                    while (!mDM.getDm_state().equals(eDM_State.RUNNING)){
                        try {
                            mDM.getDm_state().wait();
                        } catch (InterruptedException e1) {
                            throw new RuntimeException("Erro: MissionProducerThread got interrupt while wait for resume");
                        }
                    }
                }
            }
        }
        this.finish = true;
        mDM.setMissionsNumber(missionsNumber);

    }

    private void startAgents() {
        for (Agent agent : agentList){
            agent.start();
        }
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


    public void setSecretGenerator(SecretGenerator secretGenerator) {
        this.secretGenerator = secretGenerator;
    }

    public void setAgentList(List<Agent> agentList) {
        this.agentList = agentList;
    }

    public void setMissionsToCraateBeforeStartAgents(int missionsToCraateBeforeStartAgents) {
        this.missionsToCraateBeforeStartAgents = missionsToCraateBeforeStartAgents;
    }

    public boolean getFinish() {
        return finish;
    }
}
