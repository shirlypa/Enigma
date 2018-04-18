package Logic.History;

public class ProcessString {
    private String SourceStr;
    private String DestStr;
    private int Time;


    public ProcessString(String sourceStr, String destStr, int time) {
        SourceStr = sourceStr;
        DestStr = destStr;
        Time = time;
    }

    public String getSourceStr() {
        return SourceStr;
    }

    public String getDestStr() {
        return DestStr;
    }

    public int getTime() {
        return Time;
    }
}
