package Logic.MachineXMLParsser;

import Logic.MachineDescriptor.MachineComponents.Reflector;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import Logic.MachineDescriptor.MachineDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import pukteam.enigma.component.machine.api.EnigmaMachine;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MachineXMLParsser {
    public static MachineDescriptor parseMachineFromXML(String path) throws notXMLException {

        MachineDescriptor machine = new MachineDescriptor();

        if(!path.endsWith(".XML")) {
            throw new notXMLException();
        }

       try{
            File file= new File(path);
            JAXBContext jc = JAXBContext.newInstance(Enigma.class);
            Unmarshaller u= JAXBContext.createUnmarsheller();
            Enigma enigmaMachine= (Enigma)u.unmarshal(file);
       }
       catch (JAXBException e){
            e.printStackTrace();
            return null;
       }
        checkMachine(machine);
        return machine;
    }

    private static void checkMachine(MachineDescriptor machine) throws AlphabetIsOddException, InvalidRotorsInUseException, InvalidRotorsIdException, InvalidNotchLocationException, InvalidReflectorIdException {
        if(machine.getAlphabet().length() %2 !=0) {
            throw new AlphabetIsOddException();
        }
        else if(machine.getRotorsInUseCount()>machine.getAvaliableRotors().size()){
            throw new InvalidRotorsInUseException();
        }
        else if(machine.getRotorsInUseCount() <2){
            throw new InvalidRotorsInUseException();
        }
        else if(!checkRotorsIdValidation(machine)){
            throw new InvalidRotorsIdException();
        }
        else if(checkDoubleMapping(machine)!=0){

        }
        else if(checkNotchLocation(machine)!=0){
            throw new InvalidNotchLocationException();
        }
        else if (checkReflctorsId(machine.getAvaliableReflector())!=0){
            throw new InvalidReflectorIdException();
        }
        else if (checkReflectorsMapping(machine.getAvaliableReflector().values())!=0){

        }
    }

    private static int checkReflectorsMapping(Collection<Reflector> avaliableReflector) {
        for (Reflector ref: avaliableReflector) {
            for(i=0;i<ref.getDest().length;i++){
                if(ref.ge)
            }

        }
    }

    private static int checkReflctorsId(Map<Integer, Reflector> avaliableReflector) {

    }

    private static int romeToInt(String romeDig){
        if(romeDig.equals("I")){
            return 1;
        }
        else if (romeDig.equals("II")){
            return 2;
        }
        else if (romeDig.equals("III")){
            return 3;
        }
        else if (romeDig.equals("IV")){
            return 4;
        }
        else if (romeDig.equals("V")){
            return 5;
        }
        return 0;
    }

    private static int checkNotchLocation(MachineDescriptor machine) {
        Collection<Rotor> rotors = machine.getAvaliableRotors().values();
        for (Rotor r: rotors) {
            if(r.getNotch()>machine.getAlphabet().length()){
                return r.getID();
            }
        }
        return 0;
    }

    // return 0 where there is no double mapping in any rotor, otherwise return the id of the rotor
    private static int checkDoubleMapping(MachineDescriptor machine) {
        Collection<Rotor> rotors = machine.getAvaliableRotors().values();
        for (Rotor r: rotors) {
            for(int i =0;i<r.getDest().length(); i++)
            {
                //if(r.getDest().toCharArray()[i]==)
            }
        }
        return 0;
    }

    private static boolean checkRotorsIdValidation(MachineDescriptor machine) {
        Set<Integer> avaliableRotorsId = machine.getAvaliableRotors().keySet();
        ArrayList<Integer> arr=new ArrayList<Integer>();
        for (int i = 1;i<=avaliableRotorsId.size();i++)
        {
            if(!avaliableRotorsId.contains(i)){
                return false;
            }
        }
        return true;
    }


}
