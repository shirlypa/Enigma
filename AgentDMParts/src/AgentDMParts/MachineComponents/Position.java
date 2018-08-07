package AgentDMParts.MachineComponents;

import java.io.Serializable;

public class Position implements Serializable{
    private char PositionAsChar;
    private int PositionAsInt;
    private boolean IsLetter;

    public char getPositionAsChar() {
        return PositionAsChar;
    }

    public Position setPositionAsChar(char positionAsChar) {
        PositionAsChar = positionAsChar;
        return this;
    }

    public int getPositionAsInt() {
        return PositionAsInt;
    }

    public Position setPositionAsInt(int positionAsInt) {
        PositionAsInt = positionAsInt;
        return this;
    }

    public boolean isLetter() {
        return IsLetter;
    }

    public boolean setIsLetter(boolean letter) {
        IsLetter = letter;
        return IsLetter;
    }
}
