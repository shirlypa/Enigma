package Ex3.Alies;

import AgentDMParts.SuccessString;
import Ex3.update.AgentInfo;
import Logic.Dm.DM;
import Logic.Dm.eProccessLevel;
import Logic.Logic;
import org.omg.SendingContext.RunTime;

import java.io.IOException;
import java.util.*;

public class Alies {
    private Logic mLogic;
    private DM mDm;
    private String mUser;
    private boolean mReady;
    private eProccessLevel proccessLevel;
    private String roomName;
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
        mDm.startAgents();
    }

    public void stopProccess(boolean isWinner) throws IOException {
        mDm.stopAgents(isWinner);
    }

    public void setMissionSize(int missionSize) {
        //TODO
        mDm.setMissionSize(missionSize);
    }

    public int getAgentsNumber() {
        return getAgentsInfo().size();
    }
}
