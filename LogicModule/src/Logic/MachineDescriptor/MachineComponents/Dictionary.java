package Logic.MachineDescriptor.MachineComponents;

public class Dictionary {
    private String Words;
    private String[] splittedDictionary;
    private String SpecialChar;

    public Dictionary(String words, String specialChar) {
        SpecialChar = specialChar;
        Words = removeSpecialChars(words.trim());
        splittedDictionary = Words.split(" ");
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

    public String removeSpecialChars(String words){
        for (Character c : SpecialChar.toCharArray()) {
            words= words.replace(c.toString(),"");
        }
        return words;
    }

    public boolean isExistsInDictionary (String words)
    {
        words = removeSpecialChars(words);
        String [] wordsArr = words.split(" ");
        if (wordsArr.length == 0){
            return false;
        }
        for (String word:wordsArr) {
            if (!wordInDictionary(word))
                return false;

        }
        return true;
    }

    private boolean wordInDictionary(String word) {
        for (String wordInDictionary: splittedDictionary) {
            if (wordInDictionary.toUpperCase().equals(word.toUpperCase()))
                return true;
        }
        return false;
    }

    public boolean isTextValid(String userInputTxt) {
        return isExistsInDictionary(userInputTxt);
    }
}
