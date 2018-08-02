package AgentDMParts;//package Logic.Dm;

//import Logic.MachineDescriptor.MachineComponents.AgentDMParts.Secret;

public class Mission {
    private int missionID;
    private Secret initialSecret;
    private int missionSize;

    public Mission(int missionID, Secret initialSecret, int missionSize) {
        this.missionID = missionID;
        this.initialSecret = initialSecret;
        this.missionSize = missionSize;
    }

    public Secret getInitialSecret() {
        return initialSecret;
    }

    public int getMissionSize() {
        return missionSize;
    }

    @Override
    public String toString() {
        return "ID: " + missionID + "/ Initial AgentDMParts.Secret: " + initialSecret + "/ size: " + missionSize;
    }
}
