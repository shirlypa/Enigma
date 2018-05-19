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

    private void removeSpecialChars(){
        for (Character c : SpecialChar.toCharArray()) {
            Words= Words.replace(c.toString(),"");
        }
    }

    public boolean isExists (String word)
    {
        if(Words.contains(word)) {
            return true;
        }
        return false;
    }
}
