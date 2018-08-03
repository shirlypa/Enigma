package Ex3.Alies;

import Ex3.update.AgentInfo;
import Logic.Dm.DM;
import Logic.Dm.eProccessLevel;
import Logic.Logic;

import java.util.List;

public class Alies {
    private Logic mLogic;
    private DM mDm;
    private String mUser;
    private boolean mReady;
    private eProccessLevel proccessLevel;
    private int port;

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
}
