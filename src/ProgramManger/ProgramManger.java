package ProgramManger;

import Logic.Logic;
import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;
import UI.ConsoleUI.ConsoleUI;
import UI.UI;

import java.util.ArrayList;
import java.util.List;

public class ProgramManger {
    UI appUI = new ConsoleUI();
    eAppState appState = eAppState.Initial;
    Logic mLogic = new Logic();




    private void getRotorsPositionFromUser(){
        //Something like:
        /*for (int i = 0; i < Logic.RotorsInUser; i++)
        {
            do {
                Position pos = appUI.getSecretRotorPosition(rotorsInUse[i].rotorID)
                if (pos.isLetter)
                  valid = rotorsInUser[i].isValidCharacter(pos.positionAsChar)
                else
                  valid = (pos.positionAsInt >= 1 && pos.positionAsInt <= Logic.alphabet.length)
                if(!valid)
                    appUI.printError("Not valid Please Try Again");
            }while(!valid)

        }*/
    }

    private List<MenuItem> buildMenuStateDependency(){
        List<MenuItem> newMenu = new ArrayList<>();
        String firstCommandString = appState == eAppState.Initial? "Load machine from xml file" : "Load new machine";


        //add new MenuItem -> title: firstCommandString
        //add new MenuItem -> "show machine description"
        //add new MenuItem -> "select initial secret"
        //add new MenuItem -> "select Random initial secret"
        if (appState == eAppState.SecretLoaded){
            //add new MenuItem -> "process text"
            //add new MenuItem -> "reset to initial secret"
        }
        if (appState == eAppState.Started){
            //add new MenuItem -> "Show History"
        }
        //add new MenuItem -> "Exit"
        return newMenu;
    }

    private void menuCmd_loadMachindFromXML(){
        String userInputPath;
        boolean valid;
        do {
            valid = true;
            userInputPath = appUI.getXMLPath();
            if (!mLogic.loadMachineFromXml(userInputPath)){
                appUI.printError("Error: you entered invalid path. Please try again");
                valid = false;
            }
        }while(!valid);
        appUI.print("The machine loaded successfully from the XML file!");
        this.appState = eAppState.MachineLoaded;
    }

    private void menuCmd_showMachineDescription(){
        MachineDescriptor machineToPrint = mLogic.getMachineDescriptor();
        int msgProccessSoFar = mLogic.getProccesedMsgCount();
        if (appState == eAppState.SecretLoaded) //There is Secret
            appUI.printMachineDetails(machineToPrint,msgProccessSoFar,mLogic.getSecret());
        else
            appUI.printMachineDetails(machineToPrint,msgProccessSoFar);
    }

    private void menuCmd_selectInitialSecret(){

    }



}
