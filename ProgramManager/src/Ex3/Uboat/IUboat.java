package Ex3.Uboat;


import AgentDMParts.Secret;

public interface IUboat {
    String processString(Secret secret, String strToProcess,boolean random);
    String getUser();
    void setUser(String mUser);
    void setReady(boolean ready);
    boolean isReady();
}
