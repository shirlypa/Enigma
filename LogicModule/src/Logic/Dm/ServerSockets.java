package Logic.Dm;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSockets extends Thread{
    //private Monitor connection;
    private List<Socket> sockets;
    private List<ComManager> agents;
    private int port;
    private int i;

    public ServerSockets(int port) {
        this.port = port;
        sockets = new ArrayList<>();
        agents = new ArrayList<>();
        i=1;
    }

    public void run(){
        ServerSocket serverSocket = null;
        boolean done = false;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!done){
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ComManager communicationManager = new ComManager(i,socket);
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

}

