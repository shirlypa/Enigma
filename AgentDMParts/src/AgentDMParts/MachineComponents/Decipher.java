package AgentDMParts.MachineComponents;

import AgentDMParts.Dictionary;

import java.io.Serializable;

public class Decipher implements Serializable{
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