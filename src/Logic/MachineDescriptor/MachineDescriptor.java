package Logic.MachineDescriptor;

import Logic.MachineDescriptor.MachineComponents.Reflector;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import java.util.List;

public class MachineDescriptor {
    private int RotorsInUseCount;
    private String Alphabet;
    private List<Rotor> AvaliableRotors;
    private List<Reflector> AvaliableReflector;


    public MachineDescriptor(int rotorsInUseCount, String alphabet, List<Rotor> avaliableRotors, List<Reflector> avaliableReflector) {
        RotorsInUseCount = rotorsInUseCount;
        Alphabet = alphabet;
        AvaliableRotors = avaliableRotors;
        AvaliableReflector = avaliableReflector;
    }

    public int getRotorsInUseCount() {
        return RotorsInUseCount;
    }

    public String getAlphabet() {
        return Alphabet;
    }

    public List<Rotor> getAvaliableRotors() {
        return AvaliableRotors;
    }

    public List<Reflector> getAvaliableReflector() {
        return AvaliableReflector;
    }
}
