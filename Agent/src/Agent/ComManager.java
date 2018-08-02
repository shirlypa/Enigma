package Agent;

import AgentDMParts.Data;
import AgentDMParts.Dictionary;
import AgentDMParts.Mission;
import AgentDMParts.SuccessString;
import pukteam.enigma.component.machine.api.EnigmaMachine;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ComManager {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Data currentMsg;
    private Agent agent;
    private Thread listener;
    //private BlockingQueue<ResultMission> resultsQueue;
    private BlockingQueue<SuccessString> accomplishedMissionsQueue;

    private int countResultMissions = 0;
    private int port;
    private boolean logout;

    public ComManager(){
        agent = new Agent();
    }

    public void connect(String ipPortStr) {
        try{
            String[] splitIpPort = ipPortStr.split(":");
            port = Integer.parseInt(splitIpPort[1]);
            socket = new Socket(splitIpPort[0] , port);

            OutputStream outputStream = socket.getOutputStream();
            out = new ObjectOutputStream(outputStream);
            InputStream inputStream = socket.getInputStream();
            in = new ObjectInputStream(inputStream);
            System.out.println("connect");

        } catch (NumberFormatException e){
            System.out.println("I'm very sorry but the input string isn't an integer");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException, ClassNotFoundException {
        resetListener();
        boolean done = false;

        while (!done){
            currentMsg = (Data) in.readObject();
            Data.eDataType type = currentMsg.getmDataType();
            System.out.println("1. "+type);
            done = doMsg(type);
        }

        if(!logout){
            try {
                listener.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        socket.close();
        System.out.println("BYE BYE FROM AGENT");
    }

    private void resetListener() {
        listener = new Thread(()-> {
            synchronized (accomplishedMissionsQueue){
                while(accomplishedMissionsQueue.size()!= 0 || agent.isAlive()){
                    SuccessString result = accomplishedMissionsQueue.poll();
                    if(result != null){
                        countResultMissions++;
                        Data<SuccessString> msg = new Data(result,Data.eDataType.SUCCESS_STRING);
                        sendMessage(msg);
                    }
                }

                //Data msg = new Data(Data.eDataType.AgentFinishDecipher);
                //sendMessage(msg);
            }
        });
    }

    public boolean doMsg(Data.eDataType msg) throws IOException {
        switch (msg){
            //Todo all kind of messages
            case DICTIONERY:
                agent.setDictionary((Dictionary)currentMsg.getmData());
                break;
            case MACHINE:
                agent.setMachine((EnigmaMachine)currentMsg.getmData());
                break;
            case SOURCE:
                agent.setSource((String)currentMsg.getmData());
                break;
            case ALPHABET:
                agent.setAlphaBet((String)currentMsg.getmData());
                break;
            case MISSION_TODO:
                agent.setMissionQueue((BlockingQueue<Mission>)currentMsg.getmData());
                break;
            case CLOSE:
                killAgent();
                logout = true;
                sendMessage(new Data("", Data.eDataType.CLOSE));
                return true;
        }
        return false;
    }

    private void killAgent() {
        if(agent.isAlive()){
            //TODO KILL THIS AGENT
            try {
                agent.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(Data msg){
        synchronized (out){
            try {
                out.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
