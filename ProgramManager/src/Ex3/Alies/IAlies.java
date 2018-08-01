package Ex3.Alies;

import Ex3.update.AgentInfo;

import java.util.List;

public interface IAlies {
    List<String> getSuccessStrings();
    void setMissionSize(int missionSize);
    int getPortForAgentsToConnect();
    void setReady(boolean ready);
    boolean isReady();
    String getUser();
    void setUser(String mUser);
    List<AgentInfo> getAgentsInfo();
}
