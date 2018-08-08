package Agent;

import AgentDMParts.*;
import pukteam.enigma.component.machine.api.EnigmaMachine;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ComManager {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Data currentMsg;
    private Agent agent;
    private Thread listener;
    private BlockingQueue<SuccessString> accomplishedMissionsQueue;
    private BlockingQueue<Mission> missionToDo;
    private final int K_QUEUE_SIZE = 100;


    private int countResultMissions = 0;
    private int port;
    private boolean logout;

    public ComManager(){
        this.missionToDo = new ArrayBlockingQueue<>(K_QUEUE_SIZE);
        this.accomplishedMissionsQueue = new ArrayBlockingQueue<>(K_QUEUE_SIZE);

        agent = new Agent(missionToDo,accomplishedMissionsQueue, this);
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
            System.out.println("invalid port");
        }
    }

    public void run() throws IOException, ClassNotFoundException {
        resetListener();
        boolean done = false;

        while (!done){
            currentMsg = (Data) in.readObject();
            Data.eDataType type = currentMsg.getmDataType();
            //System.out.println("1. "+type);
            done = doMsg(currentMsg);
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

    public boolean doMsg(Data msg) throws IOException {
        switch (msg.getmDataType()){
            case DICTIONERY:
                agent.setDictionary((Dictionary)currentMsg.getmData());
                System.out.println("Got Dictionery");
                break;
            case MACHINE:
                agent.setMachine((MachineDescriptor)currentMsg.getmData());
                System.out.println("Got Machine");
                break;
            case SOURCE:
                agent.setSource((String)currentMsg.getmData());
                System.out.println("Got source String");
                break;
            case ALPHABET:
                agent.setAlphaBet((String)currentMsg.getmData());
                System.out.println("Got alphabet");
                break;
            case MISSION_TODO:
                //agent.setMissionQueue((BlockingQueue<Mission>)currentMsg.getmData());
                missionToDo.add((Mission) currentMsg.getmData());
                System.out.println("Got Mission");
                break;
            case START_AGENT:
                System.out.println("STARTING");
                agent.start();
                break;
            case INTERRUPT:
                agent.interrupt();
                break;
            case CLOSE:
                agent.printMsg((String)currentMsg.getmData());
                //killAgent();
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

    public void sendMessage(Data msg){
        synchronized (out){
            try {
                out.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
