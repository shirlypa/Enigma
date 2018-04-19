package Logic.MachineDescriptor.MachineComponents;

public class RotorInSecret {
    private int RotorId;
    private Position Position;

    public RotorInSecret(int rotorId, Position position) {
        RotorId = rotorId;
        Position = position;
    }

    public int getRotorId() {
        return RotorId;
    }

    public Position getPosition() {
        return Position;
    }
}
