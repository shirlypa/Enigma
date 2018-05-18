package Logic.MachineDescriptor.MachineComponents;

public class Dictionary {
    private String Words;
    private String SpecialChar;

    public Dictionary(String words, String specialChar) {
        Words = words;
        SpecialChar = specialChar;
    }

    public String getWords() {
        return Words;
    }

    public void setWords(String words) {
        Words = words;
    }

    public String getSpecialChar() {
        return SpecialChar;
    }

    public void setSpecialChar(String specialChar) {
        SpecialChar = specialChar;
    }

    public boolean isExists (String word)
    {
        return true;
    }
}
