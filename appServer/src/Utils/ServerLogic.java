package Utils;

import AgentDMParts.Secret;
import Ex3.Alies.Alies;
import Ex3.Room.Room;
import Ex3.Uboat.ProcessStringReturnValue;
import Ex3.Uboat.Uboat;
import Ex3.update.ePlayerType;
import Logic.Logic;
import Logic.MachineDescriptor.MachineDescriptor;
import Logic.MachineXMLParsser.Generated.Battlefield;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

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
    public ProcessStringReturnValue processUboatString(String uboatName, Secret secret, String strToProcess){
        return uboats.get(uboatName).processString(secret,strToProcess);
    }

    public void setUboatXml(String uboatName, MachineDescriptor machineDescriptor, Battlefield battlefield){
        Uboat currentUboat = uboats.get(uboatName);
        Room room = new Room();
        rooms.put(battlefield.getBattleName(),room);
        room.setBattlefield(battlefield);
        room.setMachineDescriptor(machineDescriptor);
        currentUboat.setMachineDescriptor(machineDescriptor);
    }

    public int login(String userName, ePlayerType playerType){
        int uboatSuccess = 1, notValid = -1;
        for (Uboat uboat : uboats.values()){
            if (uboat.getUser().equals(userName)){
                return notValid;
            }
        }
        for (Alies alies : alieses.values()){
            if (alies.getUser().equals(userName)){
                return notValid;
            }
        }

        switch (playerType){
            case Uboat:
                createNewUboat(userName);
                return uboatSuccess;
            case Alies:
                return createNewAliesAndGetPort(userName);
        }
        return notValid;
    }

    private int createNewAliesAndGetPort(String userName) {
        //TODO
        return -1;
    }

    private void createNewUboat(String userName) {
        synchronized (this){
            Uboat uboat = new Uboat();
            uboat.setUser(userName);
            uboats.put(userName,uboat);
        }
    }

    public Room getRoom(String roomName){
        return rooms.get(roomName);
    }
    public Uboat getUboat(String userName){
        return uboats.get(userName);
    }
    public Alies getAlies(String userName){
        return alieses.get(userName);
    }
}
