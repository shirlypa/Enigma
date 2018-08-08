package Utils;

import AgentDMParts.Secret;
import Ex3.Alies.Alies;
import Ex3.Room.Room;
import Ex3.Room.RoomState;
import Ex3.Room.UIRoom;
import Ex3.Uboat.Uboat;
import Ex3.update.AgentInfo;
import Ex3.update.UboatUpdate;
import Ex3.update.UiAlies;
import Ex3.update.ePlayerType;
import AgentDMParts.MachineDescriptor;
import Logic.MachineXMLParsser.Generated.Battlefield;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import java.util.*;

public class ServerLogic {
    private static ServerLogic sInstance;
    private Map<String,Room> rooms;
    private Map<String,Uboat> uboats;
    private Map<String,Alies> alieses;


    private ServerLogic() {
        rooms = new HashMap<>();
        uboats = new HashMap<>();
        alieses = new HashMap<>();
    }
    public static ServerLogic getInstance(ServletContext servletContext){
        if (sInstance == null){
            sInstance = new ServerLogic();
            servletContext.setAttribute(Consts.RoomsMap,sInstance.rooms);
            servletContext.setAttribute(Consts.UboatsMap,sInstance.uboats);
            servletContext.setAttribute(Consts.AliesesMap,sInstance.alieses);
        }
        return sInstance;
    }
    public String processUboatString(String uboatName, Secret secret, String strToProcess, boolean random){
        Uboat uboat = uboats.get(uboatName);
        rooms.get(uboat.getmRoomName()).seteRoomState(RoomState.GOT_STRING_TO_PROCESS);
        uboat.setReady(true);
        checkAllPlayersReady(uboat.getmRoomName());
        return uboat.processString(secret,strToProcess, random);
    }

    public String getUsernameFromCookies(Cookie[] cookies){
        if (cookies == null) {
            return null;
        }
        String userName = null;
        for (Cookie c : cookies) {
            if (c.getName().equals("userName"))
                userName = c.getValue();
        }
        return userName;
    }

    public void setUboatXml(String uboatName, MachineDescriptor machineDescriptor){
        synchronized (this) {
            Uboat currentUboat = uboats.get(uboatName);
            Room room = new Room();
            rooms.put(machineDescriptor.getBattlefield().getBattleName(), room);
            room.setBattlefield(machineDescriptor.getBattlefield());
            room.setMachineDescriptor(machineDescriptor);
            currentUboat.setMachineDescriptor(machineDescriptor);
            currentUboat.setmRoomName(room.getBattlefield().getBattleName());
        }
    }

    public boolean login(String userName, ePlayerType playerType){
        int uboatSuccess = 1;
        for (Uboat uboat : uboats.values()){
            if (uboat.getUser().equals(userName)){
                return false;
            }
        }
        for (Alies alies : alieses.values()){
            if (alies.getUser().equals(userName)){
                return false;
            }
        }

        switch (playerType){
            case Uboat:
                createNewUboat(userName);
                return true;
            case Alies:
                createNewAliesAndGetPort(userName);
                return true;
        }
        return false;
    }

    private void createNewAliesAndGetPort(String userName) {
        //TODO
        Alies alies=new Alies();
        alies.setUser(userName);
        synchronized (this) {
            alieses.put(userName, alies);
        }
    }

    private void createNewUboat(String userName) {
        synchronized (this){
            Uboat uboat = new Uboat();
            uboat.setUser(userName);
            uboats.put(userName,uboat);
        }
    }

    public Room getRoomByUsername(String userName){
        return rooms.get(uboats.get(userName).getmRoomName());
    }
    public Uboat getUboat(String userName){
        return uboats.get(userName);
    }
    public Alies getAlies(String userName){
        return alieses.get(userName);
    }
    public UboatUpdate getUboatUpdate(String userName){
        UboatUpdate resUpdate = new UboatUpdate();
        List<UiAlies> uiAlieses = new ArrayList<>();
        Uboat uboat = uboats.get(userName);
        String roomName = uboat.getmRoomName();
        Room room = rooms.get(roomName);
        List<Alies> aliesList = findAllAliesInRoom(roomName);
        Map<String,List<String>> successStrList = new HashMap<>();
        for (Alies alies : aliesList){
            UiAlies uiAlies = new UiAlies(userName,alies.getAgentsNumber(),alies.isReady());
            successStrList.putAll(alies.getSuccessedList());
            uiAlieses.add(uiAlies);
        }
        resUpdate.setAliesList(uiAlieses);
        room.checkWinner(successStrList);
        RoomState roomState = room.geteRoomState();
        resUpdate.setGameState(roomState);
        if (roomState.equals(RoomState.GAME_OVER)){
            resUpdate.setWinners(room.getWinners());
        }
        resUpdate.setSuccessedStrings(successStrList);
        return resUpdate;
    }

    private List<Alies> findAllAliesInRoom(String roomName){
        List<Alies> resList = new ArrayList<>();
        for (Alies alies : alieses.values()){
            if (alies.getRoomName().equals(roomName)){
                resList.add(alies);
            }
        }
        return resList;
    }
    private Uboat findUboatByRoomName(String roomName){
        for (Uboat uboat : uboats.values()) {
            if (uboat.getmRoomName().equals(roomName)){
                return  uboat;
            }
        }
        return null;
    }

    private void checkAllPlayersReady(String roomName){
        List<Alies> aliesInRoom = findAllAliesInRoom(roomName);
        Room room = rooms.get(roomName);
        if (aliesInRoom.size() < room.getBattlefield().getAllies()) return;

        boolean allReady = findUboatByRoomName(roomName).isReady();
        for (Alies alies : aliesInRoom){
            allReady &= alies.isReady();
        }
        if (!allReady) return;

        for (Alies alies : aliesInRoom){
            alies.startProcess();
        }
        room.seteRoomState(RoomState.RUNNING);
    }

    public List<UIRoom> getAvailableRooms() {
        List<UIRoom> resRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            RoomState roomState = room.geteRoomState();
            if (roomState.equals(RoomState.RUNNING) || roomState.equals(RoomState.GAME_OVER)){
                continue;
            }
            UIRoom uiRoom = new UIRoom();
            String roomName = room.getBattlefield().getBattleName();
            uiRoom.setRoomName(roomName);
            uiRoom.setProcessLevel(room.getBattlefield().getLevel());
            uiRoom.setRequiredAllies(room.getBattlefield().getAllies());
            uiRoom.setRegisteredAllies(findAllAliesInRoom(roomName).size());
            uiRoom.setUboatName(findUboatByRoomName(roomName).getUser());
            resRooms.add(uiRoom);
        }
        return resRooms;
    }

    public int getAliesPort(String userName) {
        return new Random().nextInt();
        //return alieses.get(userName).getPort();
    }

    public boolean linkAlliesToRoom(String userName, String roomName) {
        synchronized (this) {
            int reqAllies = rooms.get(roomName).getBattlefield().getAllies();
            int inRoom = findAllAliesInRoom(roomName).size();
            if (inRoom >= reqAllies) return false;
            //alieses.get(userName).setRoomName(roomName);
            return true;
        }
    }

    public void setAliesMissionSize(String userName,int missionSize) {
        //alieses.get(userName).setMissionSize(missionSize);
    }
}
