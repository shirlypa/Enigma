package Ex3.Room;

import Ex3.Uboat.ProcessStringReturnValue;
import Ex3.update.Update;
import Logic.MachineDescriptor.MachineComponents.Secret;

public interface IRoom {
    boolean isUserExist(String name); //for know if name is valid
    boolean isRoomFull(); //for know if show this room
    int addAlies(String userName); //return port
    void setReady(String userName);
    ProcessStringReturnValue processString(Secret secret, String strToProcess);
    Update getAliesUpdate(String userName);
    Update getUboatUpdate();
    void setMissionSize(String aliesUserName, int missionSize);

}
