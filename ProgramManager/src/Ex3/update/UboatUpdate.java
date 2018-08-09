package Ex3.update;

import AgentDMParts.SuccessString;
import Ex3.Room.RoomState;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UboatUpdate {
    private RoomState gameState;
    private List<UiAlies> aliesList;
    private Map<String,List<String>> successedStrings;
    private Set<String> winners;

    public RoomState getGameState() {
        return gameState;
    }

    public void setGameState(RoomState gameState) {
        this.gameState = gameState;
    }

    public List<UiAlies> getAliesList() {
        return aliesList;
    }

    public void setAliesList(List<UiAlies> aliesList) {
        this.aliesList = aliesList;
    }

    public Map<String, List<String>> getSuccessedStrings() {
        return successedStrings;
    }

    public void setSuccessedStrings(Map<String, List<String>> successedStrings) {
        this.successedStrings = successedStrings;
    }

    public Set<String> getWinners() {
        return winners;
    }

    public void setWinners(Set<String> winners) {
        this.winners = winners;
    }
}
