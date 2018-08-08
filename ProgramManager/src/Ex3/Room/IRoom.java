package Ex3.Room;

import AgentDMParts.Secret;
import Ex3.update.AliesUpdate;
import Ex3.update.UboatUpdate;

public interface IRoom {
    boolean isUserExist(String name); //for know if name is valid
    boolean isRoomFull(); //for know if show this room
    int addAlies(String userName); //return port
    void setReady(String userName);
    AliesUpdate getAliesUpdate(String userName);
    UboatUpdate getUboatUpdate();
    void setMissionSize(String aliesUserName, int missionSize);

}
