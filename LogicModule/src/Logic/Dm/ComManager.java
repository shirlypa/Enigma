package Logic.Dm;


import AgentDMParts.Data;
import AgentDMParts.Mission;
import AgentDMParts.SuccessString;
//import common.*;
//import machine.machineLogic.EnigmaMachine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ComManager extends Thread {
    // private BlockingQueue<ResultMission> resultsQueue;
    private BlockingQueue<SuccessString> validStringQueue;

    private Socket finalAgentSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public ComManager(int id, Socket finalAgentSocket) {
        this.finalAgentSocket = finalAgentSocket;
        //this.resultsQueue = new LinkedBlockingQueue<>();
        //this.missions = new ArrayList<>();
        //agentId = id;
        //permissionToSendMissions = new Boolean(false);

        try {
            in = new ObjectInputStream((finalAgentSocket.getInputStream()));
            out = new ObjectOutputStream(finalAgentSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        boolean finish = false;

        try {
            while (!finish) {
                Data<?> msg = (Data) in.readObject();
                switch (msg.getmDataType()) {
                    // TODO check which kind of data!!
                    case SUCCESS_STRING:
                        SuccessString successString = (SuccessString) msg.getmData();
                        this.validStringQueue.add(successString);
                        break;
                    case CLOSE:
                        finish = true;
                        out.close();
                        in.close();
                        finalAgentSocket.close();
                        break;


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public ObjectOutputStream getOut() {
        return out;
    }

    //TODO
    //public void sendMissions(Message<LinkedList<Mission>> msg) throws IOException {
    //    missions = msg.getParametersToFunction();
    //    synchronized (out) {
    //        out.writeObject(msg);
    //        out.flush();
    //    }
    //}


    public Socket getFinalAgentSocket() {
        return finalAgentSocket;
    }

    public void setFinalAgentSocket(Socket finalAgentSocket) {
        this.finalAgentSocket = finalAgentSocket;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

}
