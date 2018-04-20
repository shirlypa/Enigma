package ProgramManger;

import UI.ConsoleUI.ConsoleUI;
import UI.UI;

import java.util.ArrayList;
import java.util.List;

public class ProgramManger {
    UI appUI = new ConsoleUI();
    eAppState appState;




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

        if (appState == eAppState.Initial){
            //add new MenuItem -> "load machine from xml"
            return newMenu;
        }
        //add new MenuItem -> "load new Machine" (Set State = initial; and start from beginning)
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



}
