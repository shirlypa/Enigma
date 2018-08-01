package Ex3.Alies;

import Ex3.update.AgentInfo;
import Logic.Dm.DM;
import Logic.Logic;

import java.util.List;

public class Alies implements  IAlies {
    private Logic mLogic;
    private DM mDm;
    private String mUser;
    private boolean mReady;

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

    @Override
    public List<AgentInfo> getAgentsInfo() {
        return null;
    }

    @Override
    public String[] getSuccessStrings() {
        return new String[0];
    }

    @Override
    public void setMissionSize(int missionSize) {

    }

    @Override
    public int getPortForAgentsToConnect() {
        return 0;
    }
}
