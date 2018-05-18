package Logic.MachineDescriptor.MachineComponents;

import javafx.geometry.Pos;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import Logic.MachineDescriptor.MachineComponents.RotorInSecret;
import pukteam.enigma.component.machine.secret.SecretBuilder;

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
    public pukteam.enigma.component.machine.api.Secret advanceSecretAndSetOnMachine(String alphabet, EnigmaMachine machine){
        advanceRotors(alphabet);
        return createEnigmaMachineSecret(machine);
    }

    private void advanceRotors(String alphabet) {
        //TODO count on (alphabet.length)-base
    }
}
