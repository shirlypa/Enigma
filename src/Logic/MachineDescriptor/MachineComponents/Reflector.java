package Logic.MachineDescriptor.MachineComponents;

import java.util.List;

public class Reflector {
    private int ID;
    private List<Integer> Source;
    private List<Integer> Dest;

    public Reflector(int id, List<Integer> source, List<Integer> dest) {
        ID = id;
        Source = source;
        Dest = dest;
    }

    public int getID() {
        return ID;
    }

    public List<Integer> getSource() {
        return Source;
    }

    public List<Integer> getDest() {
        return Dest;
    }
}
