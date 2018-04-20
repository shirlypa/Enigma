package Logic.MachineDescriptor.MachineComponents;

public class Rotor {
    private int ID;
    private String Source;
    private String Dest;
    private int Notch;

    public Rotor(int id, String source, String dest, int notch) {
        ID = id;
        Source = source;
        Dest = dest;
        Notch = notch;
    }

    public int getID() {
        return ID;
    }

    public String getSource() {
        return Source;
    }

    public String getDest() {
        return Dest;
    }

    public int getNotch() {
        return Notch;
    }

    public boolean fillNumberWhileValidCharacter(Position position){
        int positionIndex1Base = Source.indexOf(position.getPositionAsChar()) + 1;
        if (positionIndex1Base < 1)
            return false;
        position.setPositionAsInt(positionIndex1Base);
        return true;
    }

    public boolean fillCharacterWhileValidNumber(Position position){
        int selectedPosition = position.getPositionAsInt();
        if (selectedPosition < 1 || selectedPosition > Source.length())
            return false;
        position.setPositionAsChar(Source.charAt(selectedPosition - 1));//from 1Base to 0Base
        return true;
    }
}
