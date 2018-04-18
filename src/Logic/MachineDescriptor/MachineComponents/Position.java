package Logic.MachineDescriptor.MachineComponents;

public class Position {
    private Object Position;
    private boolean IsLetter;

    public Position(Object position, boolean isLetter) {
        Position = position;
        IsLetter = isLetter;
    }

    public Object getPosition() {
        return Position;
    }

    public boolean isLetter() {
        return IsLetter;
    }
}
