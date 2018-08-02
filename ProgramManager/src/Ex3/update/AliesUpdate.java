package Ex3.update;

import Ex3.Room.RoomState;

import java.util.List;

public class AliesUpdate {
    private RoomState gameState;
    private List<UiAlies> otherAlies;
    private String uboatName;
    private boolean uboatReady;
    private List<AgentInfo> agents;
    private List<String> winners;
    private String strToProccess;

    public RoomState getGameState() {
        return gameState;
    }

    public void setGameState(RoomState gameState) {
        this.gameState = gameState;
    }

    public List<UiAlies> getOtherAlies() {
        return otherAlies;
    }

    public void setOtherAlies(List<UiAlies> otherAlies) {
        this.otherAlies = otherAlies;
    }

    public String getUboatName() {
        return uboatName;
    }

    public void setUboatName(String uboatName) {
        this.uboatName = uboatName;
    }

    public boolean isUboatReady() {
        return uboatReady;
    }

    public void setUboatReady(boolean uboatReady) {
        this.uboatReady = uboatReady;
    }

    public List<AgentInfo> getAgents() {
        return agents;
    }

    public void setAgents(List<AgentInfo> agents) {
        this.agents = agents;
    }

    public List<String> getWinners() {
        return winners;
    }

    public void setWinners(List<String> winners) {
        this.winners = winners;
    }

    public String getStrToProccess() {
        return strToProccess;
    }

    public void setStrToProccess(String strToProccess) {
        this.strToProccess = strToProccess;
    }
}
