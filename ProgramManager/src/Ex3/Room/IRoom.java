package Ex3.Room;

import AgentDMParts.Secret;
import Ex3.Uboat.ProcessStringReturnValue;
import Ex3.update.AliesUpdate;
import Ex3.update.UboatUpdate;
import Logic.Dm.eProccessLevel;

public interface IRoom {
    boolean isUserExist(String name); //for know if name is valid
    boolean isRoomFull(); //for know if show this room
    int addAlies(String userName); //return port
    void setReady(String userName);
    ProcessStringReturnValue processString(Secret secret, String strToProcess, eProccessLevel proccessLevel);
    AliesUpdate getAliesUpdate(String userName);
    UboatUpdate getUboatUpdate();
    void setMissionSize(String aliesUserName, int missionSize);

}
