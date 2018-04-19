package Logic.History;


import Logic.MachineDescriptor.MachineComponents.Secret;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class History {
    private Map<Secret, List<ProcessString>> historyDB;
    private int counter = 0;
    public int getProccesedMsgCount(){
            return counter;
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

