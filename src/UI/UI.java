package UI;

import Logic.MachineDescriptor.MachineComponents.Position;
import Logic.MachineDescriptor.MachineDescriptor;

import java.util.List;

public interface UI {
    void print(String content);
    void print(String title, String content);
    //void print(String title, String content, List<MenuItem> menu);
    //List<MenuItem> getMenu();
    void printMachineDetails(MachineDescriptor machineDescriptor);
    String getXMLPath();
    int[] getSecretRotorsInUse();
    int getReflectorInUse();
    //could be letter or digit
    Position[] getRotorsPosition();
    String getTextToProccess();
    void printError(String errorMsg);


}
