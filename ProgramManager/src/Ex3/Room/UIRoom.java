package Ex3.Room;

public class UIRoom {
    private String roomName;
    private String uboatName;
    private String processLevel;
    private int registeredAllies;
    private int requiredAllies;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUboatName() {
        return uboatName;
    }

    public void setUboatName(String uboatName) {
        this.uboatName = uboatName;
    }

    public String getProcessLevel() {
        return processLevel;
    }

    public void setProcessLevel(String processLevel) {
        this.processLevel = processLevel;
    }

    public int getRegisteredAllies() {
        return registeredAllies;
    }

    public void setRegisteredAllies(int registeredAllies) {
        this.registeredAllies = registeredAllies;
    }

    public int getRequiredAllies() {
        return requiredAllies;
    }

    public void setRequiredAllies(int requiredAllies) {
        this.requiredAllies = requiredAllies;
    }
}
