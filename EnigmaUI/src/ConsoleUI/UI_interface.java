package ConsoleUI;

import Logic.Dm.WorkSummery;
import Logic.Dm.eProccessLevel;
import Logic.History.ProcessString;
import Logic.MachineDescriptor.MachineComponents.Position;
import AgentDMParts.Secret;
import Logic.MachineDescriptor.MachineDescriptor;


import java.util.List;
import java.util.Map;

public interface UI_interface {
    void print(String content);
    void print(String title, String content);
    void print(String title, String content, List<MenuItem> menu);
    void printMachineDetails(MachineDescriptor machineDescriptor, int msgsPassedSoFar);
    void printMachineDetails(MachineDescriptor machineDescriptor, int msgsPassedSoFar, Secret secret);
    String getXMLPath();
    int[] getSecretRotorsInUse(int rotorsInUse, int maxRotorID);

    int getSecretReflectorInUse(int availableReflectorsCount);
    Position getSecretRotorPosition(int rotorID); //when ProgramManager get the Position it should use the logic to
                                    //fill the character of each position-object where .IsLetter = false
    String getTextToProccess(String alphabet);
    void printError(String errorMsg);

    void showHistory(Map<Secret, List<ProcessString>> history);

    String getTextToBruteForce();
    eProccessLevel getProccessLevel();
    int getAgentsNumber(int maxAgents);

    int getMissionSize(long workSize, int agentNum);
    void askUserForStartDesipher();
    void showDesipherStatus(WorkSummery workSummery);

}
