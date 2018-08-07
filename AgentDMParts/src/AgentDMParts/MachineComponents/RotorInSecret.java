package AgentDMParts.MachineComponents;

import java.io.Serializable;

public class RotorInSecret implements Serializable{
    private int RotorId;
    private AgentDMParts.MachineComponents.Position Position;


    public RotorInSecret(){}

    public RotorInSecret(int rotorId, AgentDMParts.MachineComponents.Position position) {
        RotorId = rotorId;
        Position = position;
    }

    public RotorInSecret(int rotorId) {
        RotorId = rotorId;
    }

    public int getRotorId() {
        return RotorId;
    }

    public AgentDMParts.MachineComponents.Position getPosition() {
        return Position;
    }

    public void setPosition(AgentDMParts.MachineComponents.Position position){
        Position = position;
    }

    public void setRotorId(int rotorId){
        RotorId = rotorId;
    }

}
