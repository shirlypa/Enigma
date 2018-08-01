package Ex3.Room;

import Ex3.Alies.Alies;
import Ex3.Alies.IAlies;
import Ex3.Uboat.IUboat;
import Ex3.Uboat.ProcessStringReturnValue;
import Ex3.Uboat.Uboat;
import Ex3.update.Update;
import Ex3.update.User;
import Ex3.update.ePlayerType;
import Logic.MachineDescriptor.MachineComponents.Secret;

import java.util.ArrayList;
import java.util.List;

public class Room implements IRoom{
    private IUboat mUboat;
    private List<IAlies> mAliesList;
    private RoomState eRoomState = RoomState.BATTLEFIELD_LOADED;
    private int mRoomMaxAlies;
    private String mStringToProcess;

    public Room(IUboat mUboat, List<IAlies> mAliesList) {
        this.mUboat = mUboat;
        this.mAliesList = mAliesList;
    }

    public ProcessStringReturnValue processString(Secret secret, String str){
        ProcessStringReturnValue res = mUboat.processString(secret,str);
        if (res.isSuccess()){
            mStringToProcess = str;
        }
        return res;
    }

    @Override
    public Update getAliesUpdate(String userName) {
        IAlies reqAlies = findAlies(userName);
        Update resUpdate = new Update();

        resUpdate.setAgentList(reqAlies.getAgentsInfo());
        resUpdate.setSuccessStrings(reqAlies.getSuccessStrings());

        List<User> userList = new ArrayList<>(mAliesList.size() + 1);
        userList.add(new User(mUboat.getUser(),mUboat.isReady(), ePlayerType.Uboat));
        for (IAlies alies : mAliesList) {
            userList.add(new User(alies.getUser(),alies.isReady(),ePlayerType.Alies));
        }
        resUpdate.setUsers(userList);

        resUpdate.setGameState(eRoomState);

        if (!eRoomState.equals(RoomState.BATTLEFIELD_LOADED)){
            resUpdate.setStringToProcess(mStringToProcess);
        }
        return resUpdate;
    }

    @Override
    public Update getUboatUpdate() {
        return null;
    }

    @Override
    public void setMissionSize(String aliesUserName, int missionSize) {
        findAlies(aliesUserName).setMissionSize(missionSize);
    }

    @Override
    public boolean isUserExist(String name){
        if (mUboat.getUser().equals(name)){
            return true;
        }
        for (IAlies alies : mAliesList){
            if (alies.getUser().equals(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRoomFull() {
        return mAliesList.size() == mRoomMaxAlies - 1; //-1 (uboat)
    }

    @Override
    public int addAlies(String userName) {
        Alies alies = new Alies();
        alies.setUser(userName);
        return alies.getPortForAgentsToConnect();
    }

    @Override
    public void setReady(String userName) {
        if (mUboat.getUser().equals(userName)){
            mUboat.setReady(true);
        } else {
            findAlies(userName).setReady(true);
        }

    }

    private IAlies findAlies(String userName){
        for(IAlies agent: mAliesList){
            if (agent.getUser().equals(userName)){
                return agent;
            }
        }
        return null;
    }
}
