package Logic.Dm;


import AgentDMParts.Data;
import AgentDMParts.Mission;
import AgentDMParts.SuccessString;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ComManager extends Thread {
    private BlockingQueue<SuccessString> validStringQueue;
    private BlockingQueue<Mission> missionTodo;
    private Socket finalAgentSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final int K_QUEUE_SIZE = 100;
    private int succsesStrings;



    public ComManager(Socket finalAgentSocket,BlockingQueue<Mission> missionTodo) {
        this.finalAgentSocket = finalAgentSocket;
        this.validStringQueue = new ArrayBlockingQueue<SuccessString>(K_QUEUE_SIZE);
        this.missionTodo = missionTodo;
        this.succsesStrings = 0;

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
                    case MISSION_TODO:
                        sendMsg(new Data(this.missionTodo.take(), Data.eDataType.MISSION_TODO));
                        break;
                    case SUCCESS_STRING:
                        SuccessString successString = (SuccessString) msg.getmData();
                        this.validStringQueue.add(successString);
                        this.succsesStrings++;
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public ObjectOutputStream getOut() {
        return out;
    }

    //TODO
    public void sendMissions(Data<Mission> msg) throws IOException {
        synchronized (out) {
            out.writeObject(msg);
            out.flush();
        }
    }

    public void startAgent() throws IOException {
        Data<String> msg = new Data<>("start", Data.eDataType.START_AGENT);
        synchronized (out) {
            out.writeObject(msg);
            out.flush();
        }
    }


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

    public void sendMsg(Data msgToSend) throws IOException {
        //Data<Object> msgToSend = new Data<>(msg, type);
        synchronized (out) {
            out.writeObject(msgToSend);
            out.flush();
        }
    }

    public int getSuccsesString() {
        return this.succsesStrings;
    }
}
