package Logic.MachineDescriptor.MachineComponents;

import AgentDMParts.Dictionary;

public class Decipher {
    private int Agents;
    private Dictionary Words;


    public Decipher(int agents, Dictionary words) {
        Agents = agents;
        Words = words;
    }


    public int getAgents() {
        return Agents;
    }

    public void setAgents(int agents) {
        Agents = agents;
    }

    public Dictionary getWords() {
        return Words;
    }

    public void setWords(Dictionary words) {
        Words = words;
    }
}