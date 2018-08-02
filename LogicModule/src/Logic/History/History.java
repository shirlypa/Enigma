package Logic.History;


import AgentDMParts.Secret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class History {
    private Map<Secret, List<ProcessString>> historyDB = new HashMap<>();
    private int counter = 0;
    public int getProccesedMsgCount(){
            return counter;
    }

    public Map<Secret, List<ProcessString>> getHistoryDB() {
        return historyDB;
    }

    public int insertRecord(Secret currentSecret, ProcessString processString){
        List<ProcessString> inputSecretMathcesStringList = historyDB.get(currentSecret);
        if (inputSecretMathcesStringList == null){
            inputSecretMathcesStringList = new ArrayList<>();
            historyDB.put(currentSecret, inputSecretMathcesStringList);
        }

        inputSecretMathcesStringList.add(processString);
        return ++counter;
    }
}

