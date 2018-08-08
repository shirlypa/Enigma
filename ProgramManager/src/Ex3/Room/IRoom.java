package Ex3.Room;

import AgentDMParts.Secret;
import Ex3.update.AliesUpdate;
import Ex3.update.UboatUpdate;

public interface IRoom {
    boolean isUserExist(String name); //for know if name is valid
    boolean isRoomFull(); //for know if show this room
    void setMissionSize(String aliesUserName, int missionSize);
}
