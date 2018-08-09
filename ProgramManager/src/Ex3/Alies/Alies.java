package Ex3.Alies;

import AgentDMParts.MachineDescriptor;
import AgentDMParts.Secret;
import AgentDMParts.SuccessString;
import Ex3.Room.Room;
import Ex3.update.AgentInfo;
import Logic.Dm.DM;
import Logic.Dm.eProccessLevel;
import Logic.Logic;
import org.omg.SendingContext.RunTime;

import java.io.IOException;
import java.util.*;

public class Alies {
    private Secret mSecret;
    private DM mDm;
    private String mUser;
    private boolean mReady;
    private eProccessLevel proccessLevel;
    private String roomName;
    private String encodedString;
    private MachineDescriptor machineDescriptor;
    private int port;

    public Alies() {

        mDm = new DM();
        this.port = mDm.getPort();

    }

    public Map<String,List<String>> getSuccessedList(){
        //TODO
        Map<String,List<String>> res = new HashMap<>();
        res.put(mUser,mDm.getValidStringList());
        return res;
    }

    public List<AgentInfo> getAgentsInfo(){
        //TODO
        List<AgentInfo> agentInfoList = new ArrayList<>();
        for (int i:mDm.getAgentInfo()) {
            agentInfoList.add(new AgentInfo(i));
        }
        return agentInfoList;
    }

    public boolean isReady() {
        return mReady;
    }

    public void setReady(boolean ready) {
        this.mReady = ready;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String mUser) {
        this.mUser = mUser;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getPort() {
        return this.port;
    }

    public void startProcess() throws IOException {
        //TODO
        mDm.setTxtToDecipher(encodedString);
        mDm.setProcessLevel(this.proccessLevel);
        mDm.setMachineDescriptor(machineDescriptor);
        mDm.setSecretGenerator();
        getKnownSecretFromUser(proccessLevel);
        mDm.ProduceMissions();
        mDm.startAgents();

        mDm.setPriority(Thread.MAX_PRIORITY);
        mDm.setName("Dm-thread");
        mDm.start();
    }

    private void getKnownSecretFromUser(eProccessLevel proccessLevel) {
        int[] currentSecretRotorsIDs = new int[machineDescriptor.getRotorsInUseCount()];
        for (int i = 0; i < currentSecretRotorsIDs.length; i++) {
            currentSecretRotorsIDs[i] =mSecret.getRotorsInUse().get(i).getRotorId();
        }
        mDm.setKnown_RotorsIDs(currentSecretRotorsIDs);
        if (!proccessLevel.equals(eProccessLevel.HARD)) {
            mDm.setKnown_RotorsOrder(true);
            if (!proccessLevel.equals(eProccessLevel.MEDIUM)){
                mDm.setKnown_Reflector(mSecret.getReflectorId());
            }
        }
    }

    public void stopProccess(boolean isWinner){
        try {
            mDm.stopAgents(isWinner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMissionSize(int missionSize) {
        //TODO
        mDm.setMissionSize(missionSize);
    }

    public int getAgentsNumber() {
        return getAgentsInfo().size();
    }

    public void setEncodedString(String encodedString) {
        this.encodedString = encodedString;
    }

    public void setProccessLevel(eProccessLevel proccessLevel) {
        this.proccessLevel = proccessLevel;
    }

    public void setMachineDescriptor(MachineDescriptor machineDescriptor) {
        this.machineDescriptor = machineDescriptor;
    }

    public void setmSecret(Secret mSecret) {
        this.mSecret = mSecret;
    }
}
