package Ex3.update;

import Ex3.Room.RoomState;

import java.util.List;
import java.util.Set;

public class AliesUpdate {
    private RoomState gameState;
    private List<UiAlies> otherAlies;
    private String uboatName;
    private boolean uboatReady;
    private Set<String> winners;
    private String strToProccess;
    private String roomName;
    private List<AgentInfo> agents;

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

    public Set<String> getWinners() {
        return winners;
    }

    public void setWinners(Set<String> winners) {
        this.winners = winners;
    }

    public String getStrToProccess() {
        return strToProccess;
    }

    public void setStrToProccess(String strToProccess) {
        this.strToProccess = strToProccess;
    }

    public void setAgents(List<AgentInfo> agents) {
        this.agents = agents;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
