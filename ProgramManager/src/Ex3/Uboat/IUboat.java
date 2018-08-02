package Ex3.Uboat;


import AgentDMParts.Secret;
import Logic.Dm.eProccessLevel;

public interface IUboat {
    ProcessStringReturnValue processString(Secret secret, String strToProcess);
    String getUser();
    void setUser(String mUser);
    void setReady(boolean ready);
    boolean isReady();
}
