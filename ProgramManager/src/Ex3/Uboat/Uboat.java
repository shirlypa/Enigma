package Ex3.Uboat;

import AgentDMParts.Dictionary;
import AgentDMParts.Secret;
import Logic.Dm.eProccessLevel;
import Logic.Logic;
import Logic.MachineDescriptor.MachineDescriptor;

public class Uboat implements IUboat {
    private Logic mLogic;
    private String mUser;
    private boolean ready;

    public ProcessStringReturnValue processString(Secret secret, String strToProcess){
        ProcessStringReturnValue res = new ProcessStringReturnValue();
        Dictionary dictionary = mLogic.getDictionary();
        if (!dictionary.isTextValid(strToProcess)){
           res.setValid(false);
           return res;
        }

        res.setValid(true);
        mLogic.setSecret(secret);
        res.setEncodedStr(mLogic.proccess(strToProcess));
        return res;
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
        this.ready = ready;
    }
}
