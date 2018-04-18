package UI;

import Logic.MachineDescriptor.MachineComponents.Position;
import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;
import ProgramManger.MenuItem;

import java.util.List;

public interface UI {
    void print(String content);
    void print(String title, String content);
    void print(String title, String content, List<MenuItem> menu);
    void printMachineDetails(MachineDescriptor machineDescriptor, int msgsPassedSoFar);
    void printMachineDetails(MachineDescriptor machineDescriptor, int msgsPassedSoFar, Secret secret);
    String getXMLPath();
    int[] getSecretRotorsInUse(int rotorsInUse, int maxRotorID);

    int getSecretReflectorInUse();
    Position getSecretRotorPosition(int rotorID); //when ProgramManager get the Position it should use the logic to
                                    //fill the character of each position-object where .IsLetter = false
    String getTextToProccess();
    void printError(String errorMsg);


}
