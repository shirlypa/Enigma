package ConsoleUI;

import Logic.Agent.SuccessString;
import Logic.Dm.WorkSummery;
import Logic.Dm.eProccessLevel;
import Logic.History.ProcessString;
import Logic.MachineDescriptor.MachineComponents.Position;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;

import java.util.*;



public class ConsoleUI implements UI_interface {
    private static final int k_width = 100;
    private static Scanner mInput = new Scanner(System.in);
    private static final String[] k_RefletorEncoding = {"I", "II", "III", "IV", "V"};
    @Override
    public void print(String content) {
            new BorderConsole(k_width,content)
                    .setAutoSizeOneLine(true)
                    .setHorizontalBorderChar('*')
                    .setVerticalBorderChar('*')
                    .print();
    }

    @Override
    public void print(String title, String content) {
            new BorderConsole(k_width)
                    .setContent(content)
                    .setTitle(title)
                    .print();
    }

    @Override
    public void print(String title, String content, List<MenuItem> menu) {
        new BorderConsole(k_width)
                .setContent(content)
                .setTitle(title)
                .setCenterConetent(true)
                .setMenu(menu)
                .print();
    }

    @Override
    public void printMachineDetails(MachineDescriptor machineDescriptor, int msgsPassedSoFar) {
        createMachineDescriptorUI(machineDescriptor,msgsPassedSoFar).print();
    }

    @Override
    public void printMachineDetails(MachineDescriptor machineDescriptor, int msgsPassedSoFar, Secret secret) {
        BorderConsole machineUIelement = createMachineDescriptorUI(machineDescriptor,msgsPassedSoFar)
                .insertNewLine("\nThe current secret: " + secret.toString()).print();
    }

    @Override
    public String getXMLPath() {
        System.out.println("Please enter the path for the XML file which represent Enigma Machine:");
        return mInput.nextLine().toUpperCase();
    }

    //validations: (1)in range 1 to i_maxRotorID (2)rotorID not showing twice
    @Override
    public int[] getSecretRotorsInUse(int rotorsInUse, int maxRotorID) {
        int[] userRotorsChoise = new int[rotorsInUse];
        HashSet duplicationSet = new HashSet();
        int userChoice;
        boolean validInput;

        for (int i = 0; i < rotorsInUse; i++)
        {
            do { //validation scope
                validInput = true;
                System.out.print("Rotor in position " + (i + 1) + "/" + rotorsInUse + " - Please enter rotorID: ");
                userChoice = getIntFromUser();
                if (userChoice  < 1|| userChoice > maxRotorID) {
                    System.out.println("You should choose number between 1 to " + maxRotorID+ ". Please try again..");
                    validInput = false;
                }
                if(!duplicationSet.add(userChoice)) //this rotor already selected
                {
                    System.out.println("The rotor ID: " + userChoice + " already selected. There is only one rotor like this.." +
                            "Please try again");
                    validInput = false;
                }
            }while(!validInput);
            userRotorsChoise[i] = userChoice;
        }
        return userRotorsChoise;
    }

    private int getIntFromUser(){
        do {
            try {
                return mInput.nextInt();
            } catch (InputMismatchException exception) {
                System.out.print(mInput.nextLine() + " isn't number.\nPlease try again: ");
            }
        } while (true);
    }

    //validations: one of the Rome Digits
    @Override
    public int getSecretReflectorInUse(int availableReflectorsCount) {
        mInput = new Scanner(System.in);
        StringBuilder stringBuilder = new StringBuilder(availableReflectorsCount);
        for (int i = 0; i < availableReflectorsCount; i++)
            stringBuilder.append(k_RefletorEncoding[i]).append(", ");

        stringBuilder.deleteCharAt(stringBuilder.length()-2);

        do {
            System.out.print("Please choose reflection (" + stringBuilder.toString() +"): ");
            String inputReflector = mInput.nextLine().trim().toUpperCase();
            for (int i = 1; i <= availableReflectorsCount; i++){
                if (inputReflector.equals(k_RefletorEncoding[i-1]) && i <= availableReflectorsCount)
                        return i;
            }
            System.out.print("You enterd invalid value. Please try again. ");
        }while(true);
    }

    @Override
    public Position getSecretRotorPosition(int rotorID) {
        Position rotorPosition = new Position();

        System.out.print("Select position for rotor id #" + rotorID + ": ");
        String userInput = new Scanner(System.in).nextLine();

        if (Character.isDigit(userInput.charAt(0))){
            rotorPosition.setPositionAsInt(new Scanner(userInput).nextInt());
            rotorPosition.setIsLetter(false);
        }
        else
        {
            rotorPosition.setPositionAsChar(Character.toUpperCase(userInput.charAt(0)));
            rotorPosition.setIsLetter(true);
        }
        return rotorPosition;
    }

    @Override
    public String getTextToProccess(String alphabet) {
        boolean valid;
        String input;
        do {
            valid = true;
            System.out.print("Please enter some text to proccess: ");
            input = mInput.nextLine().trim();
            for (char c : input.toUpperCase().toCharArray())
                if (alphabet.indexOf(c) < 0){
                    System.out.println("Error: " + c + " not appear in the alphabet: " + alphabet);
                    valid = false;
                    break;
                }
        }while(!valid);
        return input;
    }

    @Override
    public void printError(String errorMsg) {
        System.out.println(errorMsg);
    }

    @Override
    public void showHistory(Map<Secret, List<ProcessString>> history) {
        StringBuilder recordBuilder = new StringBuilder();
        BorderConsole borderConsole = new BorderConsole(k_width);
        borderConsole
                .setTitle("Enigma Machine History")
                .setPadding(6)
                .setVerticalBorderChar('+')
                .setHorizontalBorderChar('.');
        for (Secret currentSecret : history.keySet()) {
            borderConsole.insertNewLine("-- Secret: " + currentSecret);
            for (ProcessString historyRecord : history.get(currentSecret)){
                recordBuilder
                        .append("       From: '").append(historyRecord.getSourceStr())
                        .append("', To: '").append(historyRecord.getDestStr())
                        .append("' <").append(historyRecord.getTime()).append(" nS>");
                borderConsole.insertNewLine(recordBuilder.toString());
                recordBuilder = new StringBuilder();
            }
        }
        borderConsole.print();
    }

    @Override
    public String getTextToBruteForce() {
        System.out.println("Please enter string to decipher (by brute force):");
        return mInput.nextLine().trim();
    }

    private BorderConsole createMachineDescriptorUI(MachineDescriptor machineDescriptor, int msgsPassedSoFar) {
        Map<Integer,Rotor> availableRotors = machineDescriptor.getAvaliableRotors();
        StringBuilder notchStrBuilder = new StringBuilder();

        for (Rotor rotor : availableRotors.values()) {
            notchStrBuilder.append('[').append(rotor.getID()).append("](").append(rotor.getNotch()).append("), ");
        }
        notchStrBuilder.deleteCharAt(notchStrBuilder.length() - 1);
        notchStrBuilder.deleteCharAt(notchStrBuilder.length() - 1);
        notchStrBuilder.append('\n');

        return new BorderConsole(k_width)
                .setTitle("Loaded Machine Description")
                .insertNewLine("Alphabet: " + machineDescriptor.getAlphabet())
                .insertNewLine("Rotors: " + machineDescriptor.getRotorsInUseCount() + "/" + availableRotors.size())
                .insertNewLine("Notchs --[ID](Notch Position)-- :")
                .insertNewLine(notchStrBuilder.toString())
                .insertNewLine("Reflectors: " + machineDescriptor.getAvaliableReflector().size())
                .insertNewLine("This lovely machine proccessed " + msgsPassedSoFar + " so far!");

    }

    //Ex02
    public eProccessLevel getProccessLevel(){
        int i = 1;
        boolean validInput;
        int userChoice = -1;
        System.out.println("Please select the difficulty of decryption");
        for (eProccessLevel proccessLevelValue : eProccessLevel.values()) {
            System.out.print(i++);
            System.out.print(". ");
            System.out.println(proccessLevelValue.toString().toLowerCase());
        }

        int optionsSize = eProccessLevel.values().length;

        do {
            validInput = true;
            System.out.print("Your choice: ");
            try {
                userChoice = mInput.nextInt();
            } catch (InputMismatchException expection){
                System.out.print(mInput.nextLine() + " isn't number. Please try again");
                validInput = false;
            }

            if (userChoice < 1 || userChoice > optionsSize){
                System.out.println("Invalid input: You should enter a number between 1 to " + optionsSize);
                System.out.println("Please try again.");
                validInput = false;
            }
        }while(!validInput);
        return eProccessLevel.getProccesLevelByInt(userChoice);
    }

    @Override
    public int getAgentsNumber(int maxAgents) {
        boolean validInput;
        int userChoice = -1;
        do {
            validInput = true;
            System.out.println("Please Enter the number of agents (number from 2 to " + maxAgents);
            try {
                userChoice = mInput.nextInt();
            } catch (InputMismatchException expection){
                System.out.print(mInput.nextLine() + " isn't number. Please try again");
                validInput = false;
            }

            if (userChoice < 1 || userChoice > maxAgents){
                System.out.println("Invalid input: You should enter a number between 1 to " + maxAgents);
                System.out.println("Please try again.");
                validInput = false;
            }
        }while(!validInput);
        return userChoice;
    }

    @Override
    public int getMissionSize(long workSize, int agentNumber) {
        boolean validInput;
        int userChoice = -1;
        System.out.println("Please select one mission size");
        System.out.println("Hint: The number of possible combinations is: " + workSize);

        do {
            validInput = true;
            System.out.println("Enter your choice: ");
            try {
                userChoice = mInput.nextInt();
            } catch (InputMismatchException exception) {
                System.out.print(mInput.nextLine() + " isn't number. Please try again");
                validInput = false;
            }
            if (workSize / userChoice < agentNumber) {
                System.out.println("Invalid: If " + userChoice + " will be the mission size, not all agents will work");
                System.out.println("Please try again");
            }
        }while(!validInput);
        return userChoice;
    }

    @Override
    public void askUserForStartDesipher() {
        System.out.println("All set to start the process");
        System.out.println("Press any key to continue...");
        mInput.next();
    }

    public void showDesipherStatus(WorkSummery workSummery) {
        final int STRINGS_TO_SHOW = 10;
        String[] validString_info = new String[STRINGS_TO_SHOW];
        String timeForMission;
        long precent;
        int successStringListSize;

        synchronized (workSummery.getSuccessStrings()) {
            successStringListSize = workSummery.getSuccessStrings().size();
            timeForMission = workSummery.getTimeFromStart();
            precent = (workSummery.getAccomplishMissions() / workSummery.getWorkSize()) * 100;
            int successStringNumber = workSummery.getSuccessStrings().size();
            for (int i = 0; i < validString_info.length && i < successStringListSize; i++) {
                SuccessString successString = workSummery.getSuccessStrings().get(successStringNumber - 1 - i);
                validString_info[i] = ". " + successString.getSucessString()
                        + "\t" + successString.getSecretWithLuck() + "\t" + successString.getAgentID();
            }
        }
        BorderConsole borderConsole = new BorderConsole(k_width);
        borderConsole
                .setTitle("Decipher Information")
                .setPadding(6)
                .setVerticalBorderChar('+')
                .setHorizontalBorderChar('.');
        borderConsole
                .insertNewLine("Time from start the process: " + timeForMission)
                .insertNewLine("Percentage of progress: " + (int)precent + "%")
                .insertNewLine("")
                .insertNewLine("  String\tSecret\tAgent ID")
                .insertNewLine("  ======\t======\t========");
        for (int i = 0; i < validString_info.length && i < successStringListSize ; i++) {
            borderConsole.insertNewLine(validString_info[i]);
        }
        borderConsole.print();
    }
}
