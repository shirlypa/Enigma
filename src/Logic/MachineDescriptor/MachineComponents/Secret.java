package Logic.MachineDescriptor.MachineComponents;

import pukteam.enigma.component.machine.api.EnigmaMachine;

import java.util.List;

public class Secret {
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

    private class RotorInSecret{
        private int RotorId;
        private int Position;

        private RotorInSecret(int rotorId, int position) {
            RotorId = rotorId;
            Position = position;
        }

        public int getRotorId() {
            return RotorId;
        }

        public int getPosition() {
            return Position;
        }
    }

    private List<RotorInSecret> RotorsInUse;
    private int ReflectorId;

    //page 31
    public pukteam.enigma.component.machine.api.Secret createEnigmaMachineSecret(EnigmaMachine machine){
      return null;
    }
}
