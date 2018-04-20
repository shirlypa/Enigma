package Logic;

import Logic.History.History;
import Logic.History.ProcessString;
import Logic.MachineDescriptor.MachineComponents.Position;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import Logic.MachineDescriptor.MachineComponents.RotorInSecret;
import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;
import Logic.MachineXMLParsser.MachineXMLParsser;
import pukteam.enigma.component.machine.api.EnigmaMachine;

import java.util.*;

public class Logic {
    private Secret mSecret;
    private MachineDescriptor mMachineDescriptor;
    private EnigmaMachine mMachine;
    private History mHistory;

    public void setmSecret(Secret mSecret) {
        this.mSecret = mSecret;
        mSecret.createEnigmaMachineSecret(mMachine);
    }

    public void loadMachineFromXML(String path){
        mMachineDescriptor = MachineXMLParsser.parseMachineFromXML(path);
        createMachine();
    }

    private void createMachine() {

    }

    // need to keep history updated!!!!
    public String Proccess(String source) {
        String dest = mMachine.process(source);
        int time = 00000000000123456; //                 Add time calculation!!!!!!!!!!!!!!!!!!!!!!!!!@#$#@!$#%@##!!
        mHistory.insertRecord(mSecret,new ProcessString(source,dest,00000123456));
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
            char theCharInThePositionOfRotor = mMachineDescriptor.getAvaliableRotors().get(guessedRotorId).getSource().charAt(positionGuess);
            rotorInSecret = rotorInSecretMap.get(guessedRotorId);
            rotorInSecret.setPosition(new Position()
                    .setPositionAsChar(theCharInThePositionOfRotor)
                    .setPositionAsInt(positionGuess)
            );
        }
        List<RotorInSecret> rotorInSecretList = new ArrayList<RotorInSecret>(rotorInSecretMap.values());
        reflectorGuess = new Random().nextInt(mMachineDescriptor.getAvaliableReflector().size()) + 1;
        return new Secret(rotorInSecretList,reflectorGuess);
    }

    private int guessValidRotorID(Map<Integer, RotorInSecret> rotorInSecretMap) {
        boolean valid;
        int rotorIdGuess;
        RotorInSecret rotorInSecret;
        do { //pick
            valid = true;
            rotorIdGuess = new Random().nextInt(mMachineDescriptor.getAvaliableRotors().size()) + 1;
            rotorInSecret = new RotorInSecret();
            if (rotorInSecretMap.put(rotorIdGuess,rotorInSecret) == null){
                valid = false;
            }
            rotorInSecret.setRotorId(rotorIdGuess);
        }while (!valid);
        return rotorIdGuess;
    }

    public void initSecret(){

    }


    public History getyHistory() {
        return mHistory;
    }
}
