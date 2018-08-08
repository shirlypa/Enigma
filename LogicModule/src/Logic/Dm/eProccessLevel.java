package Logic.Dm;

public enum eProccessLevel {
    EASY(1),
    MEDIUM(2),
    HARD(3),
    IMPOSSIBLE(4);

    private int inMenuID;

    eProccessLevel(int inMenuID) {
        this.inMenuID = inMenuID;
    }

    public static eProccessLevel getProccesLevelByInt(int inMenuID){
        for (eProccessLevel proccessLevel : eProccessLevel.values()) {
            if (proccessLevel.inMenuID == inMenuID)
                return proccessLevel;
        }
        throw new IllegalArgumentException("Proccess Level not exist");
    }

    public static eProccessLevel fromString(String str){
        switch (str){
            case "Easy":
                return eProccessLevel.EASY;
            case "Medium":
                return eProccessLevel.MEDIUM;
            case "Hard":
                return eProccessLevel.HARD;
            case "Insane":
                return eProccessLevel.IMPOSSIBLE;
            default:
                return null;
        }
    }
}
