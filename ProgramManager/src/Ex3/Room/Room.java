package Ex3.Room;

import AgentDMParts.Secret;
import Ex3.Alies.Alies;
import Ex3.Alies.IAlies;
import Ex3.Uboat.IUboat;
import Ex3.Uboat.ProcessStringReturnValue;
import Ex3.update.AliesUpdate;
import Ex3.update.UboatUpdate;
import Ex3.update.UiAlies;
import Logic.Dm.eProccessLevel;
import Logic.Logic;
import Logic.MachineXMLParsser.Generated.Battlefield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements IRoom{
    Battlefield b;
    private String mbattleName;
    private int mRoomMaxAlies;
    private eProccessLevel mProccessLevel;
    private IUboat mUboat;
    private List<IAlies> mAliesList;
    private RoomState eRoomState = RoomState.BATTLEFIELD_LOADED;
    private Logic mLogic;
    private String mStringToProcess;
    private String mSourceString;
    private List<String> winners = new ArrayList<>();

    public ProcessStringReturnValue processString(Secret secret, String str){
        ProcessStringReturnValue res = mUboat.processString(secret,str);
        if (res.isValid()){
            mStringToProcess = res.getEncodedStr();
            mSourceString = str;
        }
        return res;
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
            resUpdate.setStrToProccess(mStringToProcess);
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
        return mAliesList.size() == mRoomMaxAlies - 1; //-1 (uboat)
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
}
