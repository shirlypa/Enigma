package UI.ConsoleUI;

import Logic.History.ProcessString;
import Logic.MachineDescriptor.MachineComponents.Position;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;
import ProgramManger.MenuItem;
import UI.UI;

import java.util.*;

public class ConsoleUI implements UI {
    private static final int k_width = 100;
    private static final Scanner mInput = new Scanner(System.in);
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
        return mInput.nextLine();
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
    public int getSecretReflectorInUse() {
        boolean validInput;
        System.out.print("Please choose reflection (I,II,III,IV,V): ");
        do {
            String inputReflector = mInput.nextLine().trim();
            for (int i = 1; i < k_RefletorEncoding.length + 1; i++){
                if (inputReflector.equals(k_RefletorEncoding[i-1]))
                    return i;
            }
            System.out.print("You enterd invalid value.\nPlease try again: ");
        }while(true);
    }

    @Override
    public Position getSecretRotorPosition(int rotorID) {
        Position rotorPosition = new Position();

        System.out.print("Select position for rotor id #" + rotorID + ": ");
        String userInput = mInput.nextLine();

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
    public String getTextToProccess() {
        boolean valid;
        String input;
        do {
            valid = true;
            System.out.print("Please enter some text to proccess (no spaces): ");
            input = mInput.nextLine().trim();
            for (char c : input.toCharArray())
                if (Character.isWhitespace(c)){
                    System.out.println("ahm ahm (no spaces). Please try again...");
                    valid = false;
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
                        .append("\tFrom: ").append(historyRecord.getSourceStr())
                        .append(" To: ").append(historyRecord.getDestStr())
                        .append(" <").append(historyRecord.getTime()).append(" nS>");
                borderConsole.insertNewLine(recordBuilder.toString());
            }
        }
        borderConsole.print();
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
}
