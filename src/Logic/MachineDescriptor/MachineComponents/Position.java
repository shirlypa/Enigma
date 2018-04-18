package Logic.MachineDescriptor.MachineComponents;

public class Position {
    private char PositionAsChar;
    private int PositionAsInt;
    private boolean IsLetter;

    public char getPositionAsChar() {
        return PositionAsChar;
    }

    public void setPositionAsChar(char positionAsChar) {
        PositionAsChar = positionAsChar;
    }

    public int getPositionAsInt() {
        return PositionAsInt;
    }

    public void setPositionAsInt(int positionAsInt) {
        PositionAsInt = positionAsInt;
    }

    public boolean isLetter() {
        return IsLetter;
    }

    public boolean setIsLetter(boolean letter) {
        IsLetter = letter;
        return IsLetter;
    }
}
