package UI.ConsoleUI;

import Logic.MachineDescriptor.MachineComponents.Position;
import Logic.MachineDescriptor.MachineDescriptor;
import UI.UI;

public class ConsoleUI implements UI {

    @Override
    public void print(String content) {

    }

    @Override
    public void print(String title, String content) {

    }

    @Override
    public void printMachineDetails(MachineDescriptor machineDescriptor) {

    }

    @Override
    public String getXMLPath() {
        return null;
    }

    @Override
    public int[] getSecretRotorsInUse() {
        return new int[0];
    }

    @Override
    public int getReflectorInUse() {
        return 0;
    }

    @Override
    public Position[] getRotorsPosition() {
        return new Position[0];
    }

    @Override
    public String getTextToProccess() {
        return null;
    }

    @Override
    public void printError(String errorMsg) {

    }
}
