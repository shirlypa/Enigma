package UI.ConsoleUI;

import ProgramManger.MenuItem;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BorderConsole {

    private final int k_MAX_WIDTH = 100;
    private int k_Width;
    private int mSpaceForText;

    private boolean mCenterConetent = false;
    private boolean mAutoSize = false;
    private String mTitle;
    private String mContent;
    private int mMarginLeft = 5;
    private int mPadding = 1;
    private char mVerticalBorderChar = '|';
    private char mHorizontalBorderChar = '=';
    private List<MenuItem> mMenu;


    public BorderConsole(int i_Width) {
        k_Width = i_Width;
        mSpaceForText = (k_Width - 2 * (mPadding + 1)); //1 for border, *2 for each side
    }

    public BorderConsole(int i_Width, String i_content) {
        k_Width = i_Width;
        mContent = i_content;
        mSpaceForText = (k_Width - 2 * (mPadding + 1)); //1 for border, *2 for each side
    }


    public BorderConsole setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public BorderConsole setMarginLeft(int mMarginLeft) {
        this.mMarginLeft = mMarginLeft;
        return this;
    }

    public BorderConsole setAutoSizeOneLine(boolean i_AutoSize) {
        if (i_AutoSize) {
            if (mContent == null) {
                throw new RuntimeException("Content must be set before set AutoSize\n");
            }

            mMenu = null;
            int requestWidth = mContent.length() + (2 * mPadding) + 2;
            if (requestWidth > k_Width)
                throw new RuntimeException("Content.length() + 2 * padding + 2 is bigger then init width (=" + k_Width + ")\n");
        }

        this.mAutoSize = i_AutoSize;
        return this;
    }

    public BorderConsole setCenterConetent(boolean mCenterConetent) {
        this.mCenterConetent = mCenterConetent;
        return this;
    }

    public BorderConsole setPadding(int i_Padding) {
        this.mPadding = i_Padding;
        mSpaceForText = (k_Width - 2 * (mPadding + 1)); //1 for border, *2 for each side
        return this;
    }

    public BorderConsole setVerticalBorderChar(char mVerticalBorderChar) {
        this.mVerticalBorderChar = mVerticalBorderChar;
        return this;
    }

    public BorderConsole setHorizontalBorderChar(char mHorizontalBorderChar) {
        this.mHorizontalBorderChar = mHorizontalBorderChar;
        return this;
    }

    public BorderConsole setMenu(List<MenuItem> mMenu) {
        this.mMenu = mMenu;
        return this;
    }

    public List<MenuItem> getmMenu() {
        return mMenu;
    }

    public BorderConsole setContent(String mContent) {
        this.mContent = mContent;
        return this;
    }

    public BorderConsole print() {
        if (mContent == null)
            throw new RuntimeException("BorderConsole: No Content Received");

        printTopBorder();
        if (mTitle != null) {
            writeLineWithSpacesAndBorders(centerString(mTitle, false)); //Print Title
            printTopBorder();
        }
        //Print Content #
        List<String> instructionPartBrokenLines = fitStringToWidth(mContent);
        for (String instructionPartBrokenLine : instructionPartBrokenLines) {
            writeLineWithSpacesAndBorders(centerString(instructionPartBrokenLine, true));
        }
        //#
        //Print Menu (if exist)
        if (mMenu != null) {
            writeLineWithSpacesAndBorders("");
            int menuSize = mMenu.size();
            for (int i = 0; i < menuSize; i++) {
                Integer lineID = i + 1;
                writeLineWithSpacesAndBorders(lineID + ". " + mMenu.get(i));
            }

        }
        //#
        if (!mAutoSize) {
            writeLineWithSpacesAndBorders("");
            writeLineWithSpacesAndBorders(centerString("--- ! ---", false));
        }
        printTopBorder();

        if (mMenu != null)
            getSelectionFromUser();
        return this;
    }

    public BorderConsole insertNewLine(String i_newLineText){
        mContent = (mContent == null? "" : mContent);
        StringBuilder stringBuilder = new StringBuilder(mContent.length() + i_newLineText.length() + 1);
        stringBuilder.append(mContent);
        stringBuilder.append('\n');
        stringBuilder.append(i_newLineText);
        mContent = stringBuilder.toString();
        return this;
    }

    private void printTopBorder() {
        System.out.print(multiChar(' ',mMarginLeft));
        int borderLength = k_Width;
        if (mAutoSize) {
            int extraMarginToCenterAll = (k_Width - (mContent.length() + 2 * mPadding + 2)) / 2;
            System.out.print(multiChar(' ', extraMarginToCenterAll));
            borderLength = mContent.length() + 2 * mPadding + 2;
        }

        System.out.print(multiChar(mHorizontalBorderChar,borderLength));
        System.out.print('\n');
    }

    private void getSelectionFromUser(){
        Scanner scanner = new Scanner(System.in);

        int userIntegerInput = 0, menuItemCount = mMenu.size();
        boolean validInput;
        do {
            validInput = true;
            System.out.print("Enter your choice: ");
            //read an integer from the console
            try {
                userIntegerInput = scanner.nextInt();
            } catch (InputMismatchException exception) {
                System.out.print(scanner.nextLine() + " isn't number. please try again.\n");
                validInput = false;
            }

            if (userIntegerInput < 1 || userIntegerInput > menuItemCount) {
                System.out.print("You should choose number between 1 to " + menuItemCount + ". Please try again.\n");
                validInput = false;
            }
        } while (!validInput);

        mMenu.get(userIntegerInput - 1).Invoke();
    }

    private String multiChar(char c, int i_times) {
        StringBuilder str = new StringBuilder(i_times);
        for (int i = 0; i < i_times; i++)
            str.append(c);
        return str.toString();
    }

    private void writeLineWithSpacesAndBorders(String i_lineContent) {
        System.out.print(multiChar(' ',mMarginLeft));
        if (mAutoSize) {
            int extraMarginToCenterAll = (k_Width - (mContent.length() + 2 * mPadding + 2)) / 2;
            System.out.print(multiChar(' ', extraMarginToCenterAll));
        }
        System.out.print(mVerticalBorderChar);
        System.out.print(multiChar(' ',mPadding));
        System.out.print(i_lineContent);
        if (!mAutoSize) //fill the rest of the line in blank
            System.out.print(multiChar(' ',mSpaceForText - i_lineContent.length()));
        System.out.print(multiChar(' ',mPadding));
        System.out.print(mVerticalBorderChar);
        System.out.print('\n');
    }



    private String centerString(String i_string, boolean i_StateDependent){

        if (mAutoSize && i_StateDependent) { //autoSize on && the input string is the content (not title)
            return i_string;
        }
        else if (!i_StateDependent || mCenterConetent) {
            int stringIndent = (mSpaceForText - i_string.length()) / 2;
            StringBuilder strBuilder = new StringBuilder(stringIndent);
            for (int i = 0; i < stringIndent; i++)
                strBuilder.append(' ');
            return strBuilder.append(i_string).toString();
        }
        else {
            return i_string;
        }
    }

    private List<String> fitStringToWidth(String i_string) {
        List<String> lineBrokenString = new ArrayList<>();
        char iterator;

        for (String lineInOriginalText : i_string.split("\n")) {
            //split at width overflow
            int currLineBeg = 0;
            int inputStrLen = lineInOriginalText.length();
            int indexToSplit = mSpaceForText;
            while (indexToSplit < inputStrLen) {
                iterator = lineInOriginalText.charAt(indexToSplit);
                if (iterator == ' ') {
                    lineBrokenString.add(lineInOriginalText.substring(currLineBeg, indexToSplit++));
                    currLineBeg = indexToSplit;
                    indexToSplit += mSpaceForText;
                } else {
                    indexToSplit--;
                }
            }
            lineBrokenString.add(lineInOriginalText.substring(currLineBeg, inputStrLen));
        }
        return lineBrokenString;
    }
}
