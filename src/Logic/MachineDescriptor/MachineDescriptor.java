package Logic.MachineDescriptor;

import Logic.MachineDescriptor.MachineComponents.Reflector;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import java.util.List;
import java.util.Map;

public class MachineDescriptor {
    private int RotorsInUseCount;
    private String Alphabet;
    private Map<Integer,Rotor> AvaliableRotors;  // map (rotorID (1Base), rotor)
    private Map<Integer,Reflector> AvaliableReflector; //map (reflectorID (1Base),reflector)



    public int getRotorsInUseCount() {
        return RotorsInUseCount;
    }

    public String getAlphabet() {
        return Alphabet;
    }

    public Map<Integer, Rotor> getAvaliableRotors() {
        return AvaliableRotors;
    }

    public Map<Integer, Reflector> getAvaliableReflector() {
        return AvaliableReflector;
    }

    public void setRotorsInUseCount(int rotorsInUseCount) {
        RotorsInUseCount = rotorsInUseCount;
    }

    public void setAlphabet(String alphabet) {
        Alphabet = alphabet;
    }

    public void setAvaliableRotors(Map<Integer, Rotor> avaliableRotors) {
        AvaliableRotors = avaliableRotors;
    }

    public void setAvaliableReflector(Map<Integer, Reflector> avaliableReflector) {
        AvaliableReflector = avaliableReflector;
    }
}
