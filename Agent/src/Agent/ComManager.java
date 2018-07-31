package Agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ComManager {
    private String host;
    private int port;
    private Socket socket;

    public ComManager(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        socket = new Socket(host,port);
    }

    public String getDataFromSocket() throws IOException {
        String line;
        StringBuilder data = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while ((line = in.readLine())!= null) {
            data.append(line);
        }
        return data.toString();
    }

    public void sendDataToSocket(String data) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        out.println(data);
    }
}
