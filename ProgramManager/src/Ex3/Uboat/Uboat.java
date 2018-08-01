package Ex3.Uboat;

import Logic.Logic;
import Logic.MachineDescriptor.MachineComponents.Dictionary;
import Logic.MachineDescriptor.MachineComponents.Secret;

public class Uboat implements IUboat {
    private Logic mLogic;
    private String mUser;
    private boolean ready;

    public ProcessStringReturnValue processString(Secret secret, String strToProcess){
        ProcessStringReturnValue res = new ProcessStringReturnValue();
        Dictionary dictionary = mLogic.getDictionary();
        if (!dictionary.isTextValid(strToProcess)){
           res.setSuccess(false);
           return res;
        }

        res.setSuccess(true);
        mLogic.setSecret(secret);
        res.setEncodedStr(mLogic.proccess(strToProcess));
        return res;
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
