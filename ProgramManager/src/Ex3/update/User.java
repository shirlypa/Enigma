package Ex3.update;

public class User {
    private String name;
    private boolean ready;
    private ePlayerType playerType;

    public User(String name, boolean ready, ePlayerType playerType) {
        this.name = name;
        this.ready = ready;
        this.playerType = playerType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public ePlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(ePlayerType playerType) {
        this.playerType = playerType;
    }
}
