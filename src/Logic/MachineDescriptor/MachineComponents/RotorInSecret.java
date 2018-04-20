package Logic.MachineDescriptor.MachineComponents;

public class RotorInSecret {
    private int RotorId;
    private Position Position;


    public RotorInSecret(){}

    public RotorInSecret(int rotorId, Position position) {
        RotorId = rotorId;
        Position = position;
    }

    public RotorInSecret(int rotorId) {
        RotorId = rotorId;
    }

    public int getRotorId() {
        return RotorId;
    }

    public Position getPosition() {
        return Position;
    }

    public void setPosition(Position position){
        Position = position;
    }

    public void setRotorId(int rotorId){
        RotorId = rotorId;
    }

}
