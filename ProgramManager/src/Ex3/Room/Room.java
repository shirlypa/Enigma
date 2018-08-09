package Ex3.Room;

import AgentDMParts.Secret;
import Ex3.Alies.Alies;
import Ex3.Alies.IAlies;
import Ex3.Uboat.IUboat;
import Ex3.update.AliesUpdate;
import Ex3.update.UboatUpdate;
import Ex3.update.UiAlies;
import Logic.Logic;
import AgentDMParts.MachineDescriptor;
import Logic.MachineDescriptor.MachineComponents.BattleFieldNew;
import Logic.MachineXMLParsser.Generated.Battlefield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements IRoom{
    private BattleFieldNew mBattlefield;
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





    private IAlies findAlies(String userName){
        for(IAlies agent: mAliesList){
            if (agent.getUser().equals(userName)){
                return agent;
            }
        }
        return null;
    }

    public BattleFieldNew getBattlefield() {
        return mBattlefield;
    }

    public void setBattlefield(BattleFieldNew mBattlefield) {
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
        synchronized (this) {
            this.eRoomState = eRoomState;
        }
    }

    public List<String> getWinners() {
        return winners;
    }

    public String getmEncodedString() {
        return mEncodedString;
    }

    public void setmEncodedString(String mEncodedString) {
        this.mEncodedString = mEncodedString;
    }

    public void setmSourceString(String mSourceString) {
        this.mSourceString = mSourceString;
    }
}
