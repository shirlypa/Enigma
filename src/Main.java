import Logic.MachineDescriptor.MachineComponents.*;
import Logic.MachineDescriptor.MachineDescriptor;

import java.util.ArrayList;
import java.util.List;

import ProgramManger.ProgramManger;
import UI.ConsoleUI.ConsoleUI;
import UI.UI;

public class Main {
    public static void main(String[] args) {
        ProgramManger controller = new ProgramManger();
        controller.run();
    }
}
