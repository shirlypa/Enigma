package Logic.MachineDescriptor.MachineComponents;

public class Reflector {
    private int ID;
    private byte[] Source;
    private byte[] Dest;

    public Reflector(int id, byte[] source, byte[] dest) {
        ID = id;
        Source = source;
        Dest = dest;
    }

    public int getID() {
        return ID;
    }

    public byte[] getSource() {
        return Source;
    }

    public byte[] getDest() {
        return Dest;
    }
}
