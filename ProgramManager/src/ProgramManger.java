

import ConsoleUI.MenuItem;
import Logic.Agent.SuccessString;
import Logic.Dm.*;
import Logic.MachineDescriptor.MachineComponents.*;
import Logic.MachineDescriptor.MachineDescriptor;
import Logic.MachineXMLParsser.*;
import Logic.Logic;

import ConsoleUI.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ProgramManger implements hasUItoShowMissions {

    private UI_interface appUI;
    private eAppState appState;
    private Logic mLogic;
    private List<MenuItem> mMenu;
    private DM dmThread;

    public ProgramManger(){
        init();
    }

    private void init() {
        appUI = new ConsoleUI();
        mLogic = new Logic();
        mMenu = new ArrayList<>();
        setAppState(eAppState.Initial);
    }

    public void run(){
        do {
            appUI.print("Enigma Machine (Java Course Ex01)","Please choose your option",mMenu);
        }while(!appState.equals(eAppState.Ended));
    }

    private void setAppState(eAppState newState){
        if (appState != newState) {
            appState = newState;
            updateMenu();
        }
    }

    private void updateMenu(){

        String firstCommandString = (appState.equals(eAppState.Initial)? "Load machine from xml file" : "Load new machine");

        //add new MenuItem -> title: firstCommandString
        if (appState.equals(eAppState.Initial)) {
            mMenu.add(new MenuItem(firstCommandString, new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    if (!appState.equals(eAppState.Initial))
                        init();
                    menuCmd_loadMachindFromXML();
                    setAppState(eAppState.MachineLoaded);
                    return null;

                }
            }));
        }

        if (appState.equals(eAppState.MachineLoaded)) {
            ((MenuItem)mMenu.get(0)).setString(firstCommandString);
            //add new MenuItem -> "show machine description"
            mMenu.add(mMenu.size() -1,new MenuItem("Show machine description", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return menuCmd_showMachineDescription();
                }
            }));
            //add new MenuItem -> "select initial secret"
            mMenu.add(mMenu.size() -1,new MenuItem("Select initial secret", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    menuCmd_selectInitialSecret();
                    if (appState.equals(eAppState.MachineLoaded))
                        setAppState(eAppState.SecretLoaded);
                    return null;
                }
            }));
            //add new MenuItem -> "select Random initial secret"
            mMenu.add(mMenu.size() -1,new MenuItem("Select random secret", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    menuCmd_randomInitialSecret();
                    if (appState.equals(eAppState.MachineLoaded))
                        setAppState(eAppState.SecretLoaded);
                    return null;
                }
            }));
        }

        if (appState.equals(eAppState.SecretLoaded)){
            //add new MenuItem -> "process text"
            mMenu.add(mMenu.size() -1,new MenuItem("Process text", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    menuCmd_processText();
                    setAppState(eAppState.Started);
                    return null;
                }
            }));

            //add new MenuItem -> "reset to initial secret"
            mMenu.add(mMenu.size() -1,new MenuItem("Reset to initial secret", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return menuCmd_restoreToInitialSecret();
                }
            }));
            mMenu.add(7,new MenuItem("Automatic decoding", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    menuCmd_bruteForceProcess();
                    setAppState(eAppState.Started);
                    return null;
                }
            }));
        }

        if (appState.equals(eAppState.Started)){
            //add new MenuItem -> "Show History"
            mMenu.add(6,new MenuItem("Show history", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return menuCmd_showHistory();
                }
            }));

        }

        //add new MenuItem -> "Exit"
        if (appState.equals(eAppState.Initial)) {
            mMenu.add(new MenuItem("Exit", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return menuCmd_exit();
                }
            }));
        }
    }

    private Void menuCmd_loadMachindFromXML(){
        String userInputPath;
        boolean valid;
        do {
            valid = true;
            userInputPath = appUI.getXMLPath();
            try {
                mLogic.loadMachineFromXml(userInputPath);
            } catch (InvalidReflectorIdException e) {
                appUI.printError("Invalid XML: Each reflector must have unique id (I-V). Please try again.");
                valid = false;
            } catch (InvalidReflectorMappingException e) {
                appUI.printError("Invalid XML: One of the reflectors reflect the same character");
                valid = false;
            } catch (notXMLException e) {
                appUI.printError("Invalid XML: Must load XML file");
                valid = false;
            } catch (InvalidRotorsIdException e) {
                appUI.printError("Invalid XML: Each rotor must have unique id, start from 1");
                valid = false;
            } catch (InvalidNotchLocationException e) {
                appUI.printError("Invalid XML: Notch can't be bigger then rotor size");
                valid = false;
            } catch (AlphabetIsOddException e) {
                appUI.printError("Invalid XML: Alphabet length must be even");
                valid = false;
            } catch (InvalidRotorsCountException e) {
                appUI.printError("Invalid XML: Rotors count can't be bigger than total amount of rotors, and can't be smaller than 2");
                valid = false;
            } catch (DoubleMappingException e) {
                appUI.printError("Invalid XML: One of the rotors Mapping the same character twice or more");
                valid = false;
            } catch (InvalidAgentsNumberException e) {
                appUI.printError("Invalid XML: The agent number must be between 2 to 50");
                valid = false;
            }
            catch (FileDoesntExistsException e){
                appUI.printError("Invalid XML: File does not exists");
                valid = false;
            }
        }while(!valid);
        appUI.print("The machine loaded successfully from the XML file!");
        return null;
    }

    private Void menuCmd_showMachineDescription(){
        MachineDescriptor machineToPrint = mLogic.getMachineDescriptor();
        int msgProccessSoFar = mLogic.getProccesedMsgCount();
        if (appState.equals(eAppState.SecretLoaded)) //There is Secret
            appUI.printMachineDetails(machineToPrint,msgProccessSoFar,mLogic.getSecret());
        else
            appUI.printMachineDetails(machineToPrint,msgProccessSoFar);
        return null;
    }

    private Void menuCmd_selectInitialSecret(){
        int[] rotorsIDSelectArr = appUI.getSecretRotorsInUse(mLogic.getRotorsInUseCount(),mLogic.getMaxRotorID());
        List<RotorInSecret> rotorInSecretList = getRotorsPositionFromUser(rotorsIDSelectArr);
        int reflectorID = appUI.getSecretReflectorInUse(mLogic.getReflectorsCount());
        mLogic.setSecret(new Secret(rotorInSecretList,reflectorID));
        return null;
    }

    private Void menuCmd_randomInitialSecret(){
        Secret randomSecret = mLogic.createRandomSecret();
        appUI.print("New Secret Generated",randomSecret.toString());
        return null;
    }

    private Void menuCmd_processText(){
        String txtToProcess = appUI.getTextToProccess(mLogic.getAlphabet()).toUpperCase();
        String result = mLogic.proccess(txtToProcess);
        appUI.print("processed text","From:\t" + txtToProcess +"\nTo:\t" + result);
        return null;
    }


    private List<RotorInSecret> getRotorsPositionFromUser(int[] rotorsIDSelectArr){
        boolean validPosition;
        int alphabetLen;
        Map<Integer,Rotor> availableRotorMap = mLogic.getAvailableRotorMap();
        Rotor selectedRotor;
        RotorInSecret rotorInSecret;
        Position selectedPos;
        List<RotorInSecret> resultList = new ArrayList<>(rotorsIDSelectArr.length);

        for (int i = 0; i < rotorsIDSelectArr.length; i++){
            rotorInSecret = new RotorInSecret(rotorsIDSelectArr[i]);
            do {
                validPosition = true;
                selectedRotor = availableRotorMap.get(rotorsIDSelectArr[i]);
                selectedPos = appUI.getSecretRotorPosition(selectedRotor.getID());
                if (selectedPos.isLetter())
                    validPosition = selectedRotor.fillNumberWhileValidCharacter(selectedPos);
                else
                    validPosition = selectedRotor.fillCharacterWhileValidNumber(selectedPos);

                if(!validPosition)
                    appUI.printError("Not valid position. Please Try Again");
            }while(!validPosition);
            rotorInSecret.setPosition(selectedPos);
            resultList.add(rotorInSecret);
        }
        return resultList;
    }

    private Void menuCmd_restoreToInitialSecret(){
        mLogic.restoreMachineToInitialSecret();
        appUI.print("The machine secret set to initial secret successfully");
        return null;
    }

    private Void menuCmd_showHistory(){
        appUI.showHistory(mLogic.getHistory());
        return null;
    }

    private Void menuCmd_bruteForceProcess(){
        String inputTxtToProccess = getTextToBruteForce();
        String encryptedTxt = mLogic.proccess(inputTxtToProccess);
        eProccessLevel proccessLevel = appUI.getProccessLevel();

        int agentsNum = appUI.getAgentsNumber(mLogic.getMaxAgents());
        dmThread = new DM(encryptedTxt, this,proccessLevel,agentsNum,mLogic.getMachineDescriptor());
        int missionSize = appUI.getMissionSize(dmThread.getWorkSize(),agentsNum);
        if (!proccessLevel.equals(eProccessLevel.IMPOSSIBLE)) {
            getKnownSecretFromUser(proccessLevel);
        }
        dmThread.setMissionSize(missionSize);
        appUI.askUserForStartDesipher();
        dmThread.setName("DM-Thread");
        dmThread.start();
        showBruteForceMenu();
        return null;
    }

    private void getKnownSecretFromUser(eProccessLevel proccessLevel) {
        int[] currentSecretRotorsIDs = new int[mLogic.getRotorsInUseCount()];
        for (int i = 0; i < currentSecretRotorsIDs.length; i++) {
            currentSecretRotorsIDs[i] = mLogic.getSecret().getRotorsInUse().get(i).getRotorId();
        }
        dmThread.setKnown_RotorsIDs(currentSecretRotorsIDs);
        if (!proccessLevel.equals(eProccessLevel.HARD)) {
            dmThread.setKnown_RotorsOrder(true);
            if (!proccessLevel.equals(eProccessLevel.MEDIUM)){
                dmThread.setKnown_Reflector(mLogic.getSecret().getReflectorId());
            }
        }
    }

    private void showBruteForceMenu() {
        List<MenuItem> menu = new ArrayList<>();

        menu.add(new MenuItem("Get decipher status", new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                WorkSummery workSummery = dmThread.createWorkSummery();
                appUI.showDesipherStatus(workSummery);
                showBruteForceMenu();
                return null;
            }
        }));

        menu.add(new MenuItem("Pause decipher process", new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                pauseDesipherProcess();
                return null;
            }
        }));

        menu.add(new MenuItem("Stop decipher process", new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                stopDecipherProcess();
                return null;
            }
        }));
        appUI.print("Desipher In process","Meanwhile, you can select one of this options",menu);
    }

    private void stopDecipherProcess() {
        dmThread.setDm_state(eDM_State.DONE);
        dmThread.interrupt();
    }

    private void pauseDesipherProcess() {
        dmThread.setDm_state(eDM_State.PAUSE);
        synchronized (dmThread){
            dmThread.notifyAll();
        }
        dmThread.interrupt();

        WorkSummery workSummery = dmThread.createWorkSummery();
        appUI.showDesipherStatus(workSummery);

        List<MenuItem> menu = new ArrayList<>();
        menu.add(new MenuItem("Resume decipher process", new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                resumeDecipherProcess();
                return null;
            }
        }));
        menu.add(new MenuItem("Stop decipher process", new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                stopDecipherProcess();
                return null;
            }
        }));

        appUI.print("Decipher Process Paused","You can select one of that options",menu);
    }

    private void resumeDecipherProcess() {
        dmThread.setDm_state(eDM_State.RUNNING);
        synchronized (dmThread) {
            dmThread.notifyAll();
        }
        showBruteForceMenu();
    }

    private Void menuCmd_exit(){
        this.setAppState(eAppState.Ended);
        appUI.print("GoodBye!");
        return null;
    }

    private String getTextToBruteForce() { //valid = all the words in the dictionary
        Dictionary dictionary = mLogic.getDictionary();
        boolean valid = true;
        String userInputTxt;
        do {
            valid = true;
            userInputTxt = appUI.getTextToBruteForce();
            if (!dictionary.isTextValid(userInputTxt)){
                appUI.printError("Invali input");
                appUI.printError("You can only enter words from the dictionry (with special characters)");
                appUI.printError("Please try again");
                valid = false;
            }
        }while(!valid);
        return userInputTxt;
    }

}
