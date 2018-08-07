package AgentDMParts;

import pukteam.enigma.component.machine.api.EnigmaMachine;

import java.io.Serializable;

public class Machine implements Serializable {
    private EnigmaMachine machine;
    public Machine(EnigmaMachine machine)
    {
        this.machine = machine;
    }

    public EnigmaMachine getMachine() {
        return machine;
    }

    public void setMachine(EnigmaMachine machine) {
        this.machine = machine;
    }
}
