package Logic;

import Logic.History.History;
import Logic.History.ProcessString;
import Logic.MachineDescriptor.MachineComponents.Secret;
import Logic.MachineDescriptor.MachineDescriptor;
import Logic.MachineXMLParsser.MachineXMLParsser;
import pukteam.enigma.component.machine.api.EnigmaMachine;

public class Logic {
    private Secret mSecret;
    private MachineDescriptor mMachineDescriptor;
    private EnigmaMachine mMachine;
    private History mHistory;

    public void setmSecret(Secret mSecret) {
        this.mSecret = mSecret;
        mSecret.createEnigmaMachineSecret(mMachine);
    }

    public void loadMachineFromXML(String path){
        mMachineDescriptor = MachineXMLParsser.parseMachineFromXML(path);
        createMachine();
    }

    private void createMachine() {

    }

    // need to keep history updated!!!!
    public String Proccess(String source) {
        String dest = mMachine.process(source);
        int time = 00000000000123456; //                 Add time calculation!!!!!!!!!!!!!!!!!!!!!!!!!@#$#@!$#%@##!!
        mHistory.insertRecord(mSecret,new ProcessString(source,dest,00000123456));
        return dest;
    }

    public MachineDescriptor getMachineDescriptor() {
        return mMachineDescriptor;
    }

    public Secret createRandomSecret(){
        return null;
    }

    public void initSecret(){

    }


    public History getyHistory() {
        return mHistory;
    }
}
