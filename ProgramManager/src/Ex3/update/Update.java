package Ex3.update;

import Ex3.Room.RoomState;

import java.util.List;

public class Update {
    private List<AgentInfo> agentList;
    private String stringToProcess;
    private RoomState gameState;
    private List<User> users;
    private List<String> successStrings;

    public List<AgentInfo> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<AgentInfo> agentList) {
        this.agentList = agentList;
    }

    public String getStringToProcess() {
        return stringToProcess;
    }

    public void setStringToProcess(String stringToProcess) {
        this.stringToProcess = stringToProcess;
    }

    public RoomState getGameState() {
        return gameState;
    }

    public void setGameState(RoomState gameState) {
        this.gameState = gameState;
    }

    public List<User> getOtherUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<String> getSuccessStrings() {
        return successStrings;
    }

    public void setSuccessStrings(List<String> successStrings) {
        this.successStrings = successStrings;
    }
}
