package Utils;

import Ex3.Room.UIRoom;

import java.util.List;

public class AvailableRoomWithPort {
    List<UIRoom> rooms;
    int port;

    public AvailableRoomWithPort(List<UIRoom> rooms, int port) {
        this.rooms = rooms;
        this.port = port;
    }
}
