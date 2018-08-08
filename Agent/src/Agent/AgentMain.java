package Agent;

import java.io.IOException;

public class AgentMain {
    public static void main(String [] args){
        ComManager com = new ComManager();
        com.connect(args[0]);
        try {
            com.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
