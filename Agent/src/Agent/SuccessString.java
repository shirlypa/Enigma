package Agent;

import Logic.MachineDescriptor.MachineComponents.Secret;

public class SuccessString {
    private String sucessString;
    private Secret secretWithLuck; // ;)
    private int agentID;

    public SuccessString(String sucessString, Secret secretWithLuck, int agentID) {
        this.sucessString = sucessString;
        this.secretWithLuck = secretWithLuck;
        this.agentID = agentID;
    }

    public String getSucessString() {
        return sucessString;
    }

    public Secret getSecretWithLuck() {
        return secretWithLuck;
    }

    public int getAgentID() {
        return agentID;
    }
}
