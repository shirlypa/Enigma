package Ex3.Uboat;

import AgentDMParts.Secret;
import Logic.Logic;
import AgentDMParts.MachineDescriptor;

public class Uboat implements IUboat {
    private Logic mLogic;
    private String mUser;
    private String mSourceString;
    private String mRoomName;
    private boolean ready;

    public String processString(Secret secret, String strToProcess,boolean random){
        if (random){
            mLogic.createRandomSecret();
        } else {
            mLogic.setSecret(secret);
        }
        return mLogic.proccess(strToProcess);
    }
    public void setMachineDescriptor(MachineDescriptor machineDescriptor){
        mLogic = new Logic();
        mLogic.setmMachineDescriptor(machineDescriptor);
    }
    public String getUser() {
        return mUser;
    }

    public void setUser(String mUser) {
        this.mUser = mUser;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        synchronized (this) {
            this.ready = ready;
        }
    }

    public String getmRoomName() {
        return mRoomName;
    }

    public void setmRoomName(String mRoomName) {
        this.mRoomName = mRoomName;
    }
}
