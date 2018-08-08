package Logic.MachineDescriptor;

import Logic.MachineDescriptor.MachineComponents.Decipher;
import Logic.MachineDescriptor.MachineComponents.Reflector;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import AgentDMParts.Dictionary;

//import Logic.MachineXMLParsser.Generated.Enigma;
import Logic.MachineXMLParsser.Generated.Battlefield;
import Logic.MachineXMLParsser.Generated.Enigma;
import Logic.MachineXMLParsser.Generated.Mapping;
import Logic.MachineXMLParsser.Generated.Reflect;
import Logic.MachineXMLParsser.MachineXMLParsser;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineDescriptor implements Serializable {
    private int RotorsInUseCount;
    private String Alphabet;
    private Map<Integer,Rotor> AvaliableRotors;  // map (rotorID (1Base), rotor)
    private Map<Integer,Reflector> AvaliableReflector; //map (reflectorID (1Base),reflector)
    private Decipher MachineDecipher;
    private Battlefield Battlefield;

    public MachineDescriptor(Enigma enigmaMachine) {
        AvaliableRotors = new HashMap<>();
        AvaliableReflector = new HashMap<>();

        this.Battlefield = enigmaMachine.getBattlefield();
        this.RotorsInUseCount=enigmaMachine.getMachine().getRotorsCount();
        this.Alphabet = enigmaMachine.getMachine().getABC().trim();
        for (Logic.MachineXMLParsser.Generated.Rotor r: enigmaMachine.getMachine().getRotors().getRotor()) {
            Rotor newRotor = new Rotor(r.getId(),getSource(r.getMapping()),getDest(r.getMapping()),r.getNotch());
            AvaliableRotors.put(r.getId(),newRotor);
        }
        for (Logic.MachineXMLParsser.Generated.Reflector r: enigmaMachine.getMachine().getReflectors().getReflector()) {
            AvaliableReflector.put(MachineXMLParsser.romeToInt(r.getId()),
                    new Reflector(MachineXMLParsser.romeToInt(r.getId()),
                            getReflectorSource(r.getReflect(),
                                    enigmaMachine.getMachine().getABC().trim().length()),
                            getReflectorDest(r.getReflect(),
                                    enigmaMachine.getMachine().getABC().trim().length())));
        }

        Dictionary dic = new Dictionary(enigmaMachine.getDecipher().getDictionary().getWords(),
                enigmaMachine.getDecipher().getDictionary().getExcludeChars());
        MachineDecipher = new Logic.MachineDescriptor.MachineComponents.Decipher(enigmaMachine.getDecipher().getAgents(),dic);
        MachineDecipher = new Decipher(enigmaMachine.getDecipher().getAgents(),
                new AgentDMParts.Dictionary(enigmaMachine.getDecipher().getDictionary().getWords(),
                enigmaMachine.getDecipher().getDictionary().getExcludeChars()));

    }

    public EnigmaMachine createMachineInstance(){
        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(RotorsInUseCount,Alphabet);
        Map<Integer,Rotor> availableRotors = AvaliableRotors;
        Map<Integer,Reflector> availableReflectors = AvaliableReflector;
        for (Rotor r:availableRotors.values()) {
            machineBuilder.defineRotor(r.getID(),r.getSource(),r.getDest(),r.getNotch());
        }

        for (Reflector r:availableReflectors.values()) {
            machineBuilder.defineReflector(r.getID(),r.getSource(),r.getDest());
        }
        return machineBuilder.create();
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
        StringBuilder stringBuilder = new StringBuilder(map.size());
        for (Mapping m : map) {
            stringBuilder.append(m.getLeft());
        }
        return stringBuilder.toString();
    }
    private String getDest(List<Mapping> map){
        StringBuilder stringBuilder = new StringBuilder(map.size());
        for (Mapping m : map) {
            stringBuilder.append(m.getRight());
        }
        return stringBuilder.toString();
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

    public Dictionary getDictionary(){
        return this.MachineDecipher.getWords();
    }

    public int getMaxAgents() {
        return this.MachineDecipher.getAgents();
    }

    public Logic.MachineXMLParsser.Generated.Battlefield getBattlefield() {
        return Battlefield;
    }
}
