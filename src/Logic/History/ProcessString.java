package Logic.History;

public class ProcessString {
    private String SourceStr;
    private String DestStr;
    private long Time;


    public ProcessString(String sourceStr, String destStr, long time) {
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

    public long getTime() {
        return Time;
    }
}
