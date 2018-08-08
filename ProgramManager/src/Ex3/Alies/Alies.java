package Ex3.Alies;

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
        List<String> strings = new ArrayList<>();
        strings.add("abc");
        strings.add("def");
        strings.add("edffdsd");
        res.put(mUser,strings);
        return res;
    }
    public int getAgentsNumber(){
        return new Random().nextInt(7);
    }

    public List<AgentInfo> getAgentsInfo(){
        //TODO
        List<AgentInfo> agentInfoList = new ArrayList<>();
        int agentNumber = new Random().nextInt(4);
        for (int i = 0; i < agentNumber; i++) {
            agentInfoList.add(new AgentInfo(new Random().nextInt(6)));
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

    public void stopProccess(){
        mDm.stopAgents();
    }

    public void setMissionSize(int missionSize) {
        //TODO
        mDm.setMissionSize(missionSize);
    }
}
