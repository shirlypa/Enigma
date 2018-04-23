package Logic.MachineDescriptor;

import Logic.MachineDescriptor.MachineComponents.Reflector;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import Logic.MachineXMLParsser.Generated.Enigma;
import Logic.MachineXMLParsser.Generated.Mapping;
import Logic.MachineXMLParsser.Generated.Reflect;
import Logic.MachineXMLParsser.MachineXMLParsser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MachineDescriptor {
    private int RotorsInUseCount;
    private String Alphabet;
    private Map<Integer,Rotor> AvaliableRotors;  // map (rotorID (1Base), rotor)
    private Map<Integer,Reflector> AvaliableReflector; //map (reflectorID (1Base),reflector)

    public MachineDescriptor(Enigma enigmaMachine) {
        this.RotorsInUseCount=enigmaMachine.getMachine().getRotorsCount();
        this.Alphabet = enigmaMachine.getMachine().getABC();
        for (Logic.MachineXMLParsser.Generated.Rotor r: enigmaMachine.getMachine().getRotors().getRotor()) {
            AvaliableRotors.put(r.getId(),new Rotor(r.getId(),getSource(r.getMapping()),getDest(r.getMapping()),r.getNotch()));
        }
        for (Logic.MachineXMLParsser.Generated.Reflector r: enigmaMachine.getMachine().getReflectors().getReflector()) {
            AvaliableReflector.put(MachineXMLParsser.romeToInt(r.getId()),
                    new Reflector(MachineXMLParsser.romeToInt(r.getId()),
                            getReflectorSource(r.getReflect(),
                                    enigmaMachine.getMachine().getABC().length()),
                            getReflectorDest(r.getReflect(),
                                    enigmaMachine.getMachine().getABC().length())));
        }
    }
    private byte[] getReflectorSource(List<Reflect> reflects,int alphabetSize){
        byte[] arr = new byte[alphabetSize / 2];
        int i=0;
        for (Reflect r:reflects) {
            arr[i]=(byte)r.getInput();
            i++;
        }
        return arr;
    }
    private byte[] getReflectorDest(List<Reflect> reflects, int alphabetSize){
        byte[] arr = new byte[alphabetSize / 2];
        int i=0;
        for (Reflect r:reflects) {
            arr[i]=(byte)r.getOutput();
            i++;
        }
        return arr;
    }
    private String getSource(List<Mapping> map){
        String res="";
        for (Mapping m:map) {
            res.concat(m.getFrom());
        }
        return res;
    }
    private String getDest(List<Mapping> map){
        String res="";
        for (Mapping m:map) {
            res.concat(m.getTo());
        }
        return res;
    }

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
