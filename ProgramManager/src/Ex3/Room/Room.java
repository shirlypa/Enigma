package Ex3.Room;

import AgentDMParts.Secret;
import Ex3.Alies.Alies;
import Ex3.Alies.IAlies;
import Ex3.Uboat.IUboat;
import Ex3.update.AliesUpdate;
import Ex3.update.UboatUpdate;
import Ex3.update.UiAlies;
import Logic.Logic;
import Logic.MachineDescriptor.MachineDescriptor;
import Logic.MachineXMLParsser.Generated.Battlefield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements IRoom{
    private Battlefield mBattlefield;
    private MachineDescriptor mMachineDescriptor;

    private IUboat mUboat;
    private List<IAlies> mAliesList;
    private RoomState eRoomState = RoomState.BATTLEFIELD_LOADED;
    private String mEncodedString;
    private String mSourceString;
    private List<String> winners = new ArrayList<>();


    public void checkWinner(Map<String,List<String>> strings){
        for (Map.Entry<String,List<String>> stringListEntry : strings.entrySet()){
            List<String> stringList = stringListEntry.getValue();
            for (String str : stringList) {
                if (str.equals(mSourceString)) {
                    synchronized (this) {
                        eRoomState = RoomState.GAME_OVER;
                        winners.add(stringListEntry.getKey());
                    }
                }
            }
        }
    }

    @Override
    public AliesUpdate getAliesUpdate(String userName) {
        IAlies reqAlies = findAlies(userName);
        AliesUpdate resUpdate = new AliesUpdate();

        resUpdate.setAgents(reqAlies.getAgentsInfo());

        List<UiAlies> userList = new ArrayList<>(mAliesList.size());
        for (IAlies alies : mAliesList) {
            if (!alies.getUser().equals(userName)){
                userList.add(new UiAlies(alies.getUser(),alies.getAgentNumber(),alies.isReady()));
            }
        }
        resUpdate.setOtherAlies(userList);
        resUpdate.setGameState(eRoomState);

        if (!eRoomState.equals(RoomState.BATTLEFIELD_LOADED)){
            resUpdate.setStrToProccess(mEncodedString);
        }
        if (winners.size() != 0) {
            resUpdate.setWinners(winners);
            //TODO stop the dm and the agents
        }
        return resUpdate;
    }

    @Override
    public UboatUpdate getUboatUpdate() {
        UboatUpdate resUpdate = new UboatUpdate();

        Map<String, List<String>> successedStr = new HashMap<>();
        List<UiAlies> resAliesList = new ArrayList<>();
        for (IAlies alies : mAliesList){
            String userName = alies.getUser();
            resAliesList.add(new UiAlies(userName,alies.getAgentNumber(),alies.isReady()));
            List<String> aliesStrings = alies.getSuccessStrings();
            successedStr.put(userName,aliesStrings);
            if (checkForWinner(aliesStrings)){
                winners.add(userName);
                eRoomState = RoomState.GAME_OVER;
            }
        }
        resUpdate.setAliesList(resAliesList);
        resUpdate.setSuccessedStrings(successedStr);

        resUpdate.setGameState(eRoomState);
        if (winners.size() != 0){
            resUpdate.setWinners(winners);
        }
        return resUpdate;
    }

    private boolean checkForWinner(List<String> successedStr) {
        for (String str : successedStr){
            if (str.equals(mSourceString)){
                return true;
            }
        }
        return false;
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
        return mAliesList.size() == mBattlefield.getAllies();
    }

    @Override
    public int addAlies(String userName) {
        Alies alies = new Alies();
        alies.setUser(userName);
        //return alies.getPortForAgentsToConnect();
        return 0;
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

    public Battlefield getBattlefield() {
        return mBattlefield;
    }

    public void setBattlefield(Battlefield mBattlefield) {
        this.mBattlefield = mBattlefield;
    }

    public MachineDescriptor getMachineDescriptor() {
        return mMachineDescriptor;
    }

    public void setMachineDescriptor(MachineDescriptor mMachineDescriptor) {
        this.mMachineDescriptor = mMachineDescriptor;
    }

    public RoomState geteRoomState() {
        return eRoomState;
    }

    public void seteRoomState(RoomState eRoomState) {
        this.eRoomState = eRoomState;
    }

    public List<String> getWinners() {
        return winners;
    }
}
