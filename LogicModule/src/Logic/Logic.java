package Logic;

import Logic.History.History;
import Logic.History.ProcessString;
import Logic.MachineDescriptor.MachineComponents.*;
import Logic.MachineDescriptor.MachineDescriptor;
import Logic.MachineXMLParsser.*;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Logic {
    private Secret mSecret;
    private MachineDescriptor mMachineDescriptor;
    private EnigmaMachine mMachine;
    private History mHistory = new History();

    public void setSecret(Secret mSecret) {
        this.mSecret = mSecret;
        mSecret.createEnigmaMachineSecret(mMachine);
    }

    public Secret getSecret() {
        return mSecret;
    }



    private void createMachine() {
       EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(mMachineDescriptor.getRotorsInUseCount(),mMachineDescriptor.getAlphabet());
       Map<Integer,Rotor> availableRotors = mMachineDescriptor.getAvaliableRotors();
       Map<Integer,Reflector> availableReflectors = mMachineDescriptor.getAvaliableReflector();
        for (Rotor r:availableRotors.values()) {
            machineBuilder.defineRotor(r.getID(),r.getSource(),r.getDest(),r.getNotch());
        }

        for (Reflector r:availableReflectors.values()) {
            machineBuilder.defineReflector(r.getID(),r.getSource(),r.getDest());
        }
        mMachine= machineBuilder.create();
    }

    public void restoreMachineToInitialSecret(){
        mMachine.resetToInitialPosition();

    }

    // keep history updated :)
    public String proccess(String source) {
        Instant start = Instant.now();
        String dest = mMachine.process(source);
        Instant end = Instant.now();
        long time = Duration.between(start,end).toMillis();
        mHistory.insertRecord(mSecret,new ProcessString(source,dest,time));
        return dest;
    }

    public MachineDescriptor getMachineDescriptor() {
        return mMachineDescriptor;
    }

    public Secret createRandomSecret() {
        Map<Integer,RotorInSecret> rotorInSecretMap = new HashMap<>();
        int guessedRotorId, positionGuess,reflectorGuess;
        RotorInSecret rotorInSecret;
        boolean valid;

        //build List<RotorsInSeret> rotorsInUser for the secret
        for (int i = 0; i < mMachineDescriptor.getRotorsInUseCount(); i++){
            guessedRotorId = guessValidRotorID(rotorInSecretMap);
            positionGuess = new Random().nextInt(mMachineDescriptor.getAlphabet().length()) + 1;
            char theCharInThePositionOfRotor = mMachineDescriptor.getAvaliableRotors().get(guessedRotorId).getSource().charAt(positionGuess -1);
            rotorInSecret = rotorInSecretMap.get(guessedRotorId);
            rotorInSecret.setPosition(new Position()
                    .setPositionAsChar(theCharInThePositionOfRotor)
                    .setPositionAsInt(positionGuess)
            );
        }
        List<RotorInSecret> rotorInSecretList = new ArrayList<RotorInSecret>(rotorInSecretMap.values());
        reflectorGuess = new Random().nextInt(mMachineDescriptor.getAvaliableReflector().size()) + 1;
        Secret guessSecret = new Secret(rotorInSecretList,reflectorGuess);
        setSecret(guessSecret);
        return guessSecret;
    }

    private int guessValidRotorID(Map<Integer, RotorInSecret> rotorInSecretMap) {
        boolean valid;
        int rotorIdGuess;
        RotorInSecret rotorInSecret;
        do {
            valid = true;
            rotorIdGuess = new Random().nextInt(mMachineDescriptor.getAvaliableRotors().size()) + 1;
            if (rotorInSecretMap.get(rotorIdGuess) != null){
                valid = false;
            }
            else{
                rotorInSecret = new RotorInSecret();
                rotorInSecret.setRotorId(rotorIdGuess);
                rotorInSecretMap.put(rotorIdGuess,rotorInSecret);
            }
        }while (!valid);
        return rotorIdGuess;
    }

    public void resetToInitialPosition(){
        mMachine.resetToInitialPosition();
    }

    public int getRotorsInUseCount(){
        return mMachineDescriptor.getRotorsInUseCount();
    }

    public int getMaxRotorID(){
        return mMachineDescriptor.getAvaliableRotors().size();
    }

    public String getAlphabet(){
        return mMachineDescriptor.getAlphabet();
    }

    public Map<Integer,Rotor> getAvailableRotorMap(){
        return mMachineDescriptor.getAvaliableRotors();
    }


    public Map<Secret, List<ProcessString>> getHistory() {
        return mHistory.getHistoryDB();
    }

    public void loadMachineFromXml(String path) throws InvalidReflectorIdException, InvalidReflectorMappingException, notXMLException, InvalidRotorsIdException, InvalidNotchLocationException, AlphabetIsOddException, InvalidRotorsCountException, DoubleMappingException, FileDoesntExistsException, InvalidAgentsNumberException {
        mMachineDescriptor = MachineXMLParsser.parseMachineFromXML(path);
        createMachine();
    }

    public int getProccesedMsgCount(){return mHistory.getProccesedMsgCount();}

    public int getReflectorsCount() {
        return mMachineDescriptor.getAvaliableRotors().size();
    }
}
