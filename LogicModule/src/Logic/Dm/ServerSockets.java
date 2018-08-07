package Logic.Dm;


import AgentDMParts.Mission;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ServerSockets extends Thread{
    private List<Socket> sockets;
    private List<ComManager> agents;
    private int port;
    private int i;
    private ServerSocket serverSocket;
    private BlockingQueue<Mission> missionTodo;

    public ServerSockets(int port, BlockingQueue<Mission> missionTodo) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.port = serverSocket.getLocalPort();
        sockets = new ArrayList<>();
        agents = new ArrayList<>();
        this.missionTodo=missionTodo;
        i=1;
    }

    public void run(){

        boolean done = false;
        while(!done){
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ComManager communicationManager = new ComManager(socket,missionTodo);
            communicationManager.start();
            i++;

            agents.add(communicationManager);
            sockets.add(socket);
        }
    }

    public List<Socket> getSockets() {
        return sockets;
    }

    public List<ComManager> getAgents() {
        return agents;
    }

    public int getPort() {
        return port;
    }
}

