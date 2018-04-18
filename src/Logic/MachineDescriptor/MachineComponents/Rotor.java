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
}
