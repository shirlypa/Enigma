package Logic.MachineDescriptor.MachineComponents;

import javafx.geometry.Pos;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import Logic.MachineDescriptor.MachineComponents.RotorInSecret;

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

    //page 31
    public pukteam.enigma.component.machine.api.Secret createEnigmaMachineSecret(EnigmaMachine machine){
      return null;
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
}
