package Logic.MachineDescriptor.MachineComponents;

public class Dictionary {
    private String Words;
    private String SpecialChar;

    public Dictionary(String words, String specialChar) {
        Words = removeSpecialChars(words);
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

    private String removeSpecialChars(String words){
        for (Character c : SpecialChar.toCharArray()) {
            words= words.replace(c.toString(),"");
        }
        return words;
    }

    public boolean isExistsInDictionary (String words)
    {
        words = removeSpecialChars(words);
        String [] wordsArr = words.split(" ");
        for (String word:wordsArr) {
            if (!Words.contains(word)) {
                return false;
            }
        }
        return true;
    }
}
