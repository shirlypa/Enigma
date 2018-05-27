package Logic.MachineDescriptor.MachineComponents;

import javafx.geometry.Pos;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import Logic.MachineDescriptor.MachineComponents.RotorInSecret;
import pukteam.enigma.component.machine.secret.SecretBuilder;

import java.util.ArrayList;
import java.util.List;

public class Secret {

    private List<RotorInSecret> RotorsInUse;
    private int ReflectorId;

    public Secret(List<RotorInSecret> rotorsInUse, int reflectorId) {
        RotorsInUse = rotorsInUse;
        ReflectorId = reflectorId;
    }

    public List<RotorInSecret> getRotorsInUse() {
        return RotorsInUse;
    }

    public int getReflectorId() {
        return ReflectorId;
    }


    public pukteam.enigma.component.machine.api.Secret createEnigmaMachineSecret(EnigmaMachine machine){
      SecretBuilder secretBuilder = machine.createSecret();
        for (int i = RotorsInUse.size() - 1; i >= 0; i--) {
            secretBuilder.selectRotor(RotorsInUse.get(i).getRotorId(),RotorsInUse.get(i).getPosition().getPositionAsChar());
        }
        secretBuilder.selectReflector(ReflectorId);
        return secretBuilder.create();
    }

    @Override
    public String toString() {
        //
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append('<');
        for (RotorInSecret rotor: RotorsInUse)
            stringBuilder.append(rotor.getRotorId()+ ",");

        stringBuilder
                .deleteCharAt(stringBuilder.length() - 1)
                .append("><");

        for (RotorInSecret rotor : RotorsInUse)
            stringBuilder.append(rotor.getPosition().getPositionAsChar());

        stringBuilder
                .append("><").append(ReflectorId).append(">");

        return stringBuilder.toString();
    }

    //Ex02
    //this function called by the agents
    public pukteam.enigma.component.machine.api.Secret advanceSecretAndSetOnMachine(String alphabet, EnigmaMachine machine){
        advanceRotors(alphabet);
        return createEnigmaMachineSecret(machine);
    }

    //this function called by the MissionProducer:
    // advance the instance of the secret and return true if the code was reset
    public boolean advanceRotors(String alphabet) {
        for (int i = RotorsInUse.size() - 1; i >= 0; i--) {
            RotorInSecret r = RotorsInUse.get(i);
            int currentPos = r.getPosition().getPositionAsInt();
            currentPos = currentPos % alphabet.length() + 1;
            r.getPosition().setPositionAsInt(currentPos)
                    .setPositionAsChar(alphabet.charAt(currentPos - 1));
            if (r.getPosition().getPositionAsInt() != 1){
                break;
            }
        }

        for (RotorInSecret r : RotorsInUse) {
            if(r.getPosition().getPositionAsInt() != 0){
                return false;
            }
        }
        return true;
    }

    private char getNextAlphabet (String alphabet, char currChar)
    {
        int next = alphabet.indexOf(currChar);
        if (next==alphabet.length() -1) {
            next = 0;
        }
        return alphabet.toCharArray()[next];
    }

    public Secret cloneSecret(){
        List<RotorInSecret> newRotorinSecretList = new ArrayList<>();
        for (RotorInSecret rotor : this.RotorsInUse){
            int rotorPositionInt = rotor.getPosition().getPositionAsInt();
            char rotorPositionChar = rotor.getPosition().getPositionAsChar();
            RotorInSecret newRotor = new RotorInSecret(rotor.getRotorId(),new Position());
            newRotor.getPosition().setPositionAsChar(rotorPositionChar);
            newRotor.getPosition().setPositionAsInt(rotorPositionInt);
            newRotorinSecretList.add(newRotor);
        }
        return new Secret(newRotorinSecretList,this.ReflectorId);
    }
}
