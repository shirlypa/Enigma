package Logic.MachineXMLParsser;

import Logic.MachineDescriptor.MachineComponents.Reflector;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import Logic.MachineDescriptor.MachineDescriptor;
import Logic.MachineXMLParsser.Generated.Enigma;
import Logic.MachineXMLParsser.Generated.Mapping;
import Logic.MachineXMLParsser.Generated.Reflect;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import pukteam.enigma.component.machine.api.EnigmaMachine;

import javax.annotation.Generated;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
//import Logic.MachineXMLParsser.Generated.Enigma;

public class MachineXMLParsser {
    public static MachineDescriptor parseMachineFromXML(String path) throws notXMLException, DoubleMappingException, InvalidNotchLocationException, InvalidReflectorMappingException, InvalidRotorsIdException, InvalidReflectorIdException, AlphabetIsOddException, InvalidRotorsCountException {
        Enigma enigmaMachine;
        if(!path.endsWith(".XML")) {
            throw new notXMLException();
        }

       try{
            File file= new File(path);
            JAXBContext jc = JAXBContext.newInstance(Enigma.class);
            Unmarshaller u= jc.createUnmarshaller();
            enigmaMachine= (Enigma)u.unmarshal(file);
       }
       catch (JAXBException e){
            e.printStackTrace();
            return null;
       }
        checkMachine(enigmaMachine);
        return createMachine(enigmaMachine);

    }

    private static MachineDescriptor createMachine(Enigma enigmaMachine) {
        MachineDescriptor machine=new MachineDescriptor(enigmaMachine);
        return machine;
    }

    private static void checkMachine(Enigma enigma) throws AlphabetIsOddException, InvalidRotorsCountException, InvalidRotorsIdException, DoubleMappingException, InvalidReflectorIdException, InvalidNotchLocationException, InvalidReflectorMappingException {
        if(enigma.getMachine().getABC().length() %2 !=0){
            throw new AlphabetIsOddException();
        }
        else if(enigma.getMachine().getRotorsCount() > enigma.getMachine().getRotors().getRotor().size()){
            throw new InvalidRotorsCountException();
        }
        else if (enigma.getMachine().getRotorsCount()<2){
            throw new InvalidRotorsCountException();
        }
        else if (checkRotorsIdValidation(enigma.getMachine().getRotors().getRotor())){
            throw new InvalidRotorsIdException();
        }
        else if (checkDoubleMapping(enigma.getMachine().getRotors().getRotor())!=0){
            throw new DoubleMappingException();
        }
        else if(checkNotchLocation(enigma.getMachine().getRotors().getRotor(),enigma.getMachine().getABC().length())!=0){
            throw new InvalidNotchLocationException();
        }
        else if(checkReflectorsId(enigma.getMachine().getReflectors().getReflector())){
            throw new InvalidReflectorIdException();
        }
        else if (checkReflectorsMapping(enigma.getMachine().getReflectors().getReflector())!=0){
            throw new InvalidReflectorMappingException();
        }
    }

    private static int checkReflectorsMapping(List<Logic.MachineXMLParsser.Generated.Reflector> reflector) {
        for (Logic.MachineXMLParsser.Generated.Reflector ref: reflector) {
            if(isReflectSameChar(ref.getReflect()))
                return romeToInt(ref.getId());

        }
        return 0;
    }

    private static boolean isReflectSameChar(List<Reflect> reflect) {
        for (Reflect ref:reflect) {
            if(ref.getInput()==ref.getOutput())
            {
                return false;
            }
        }
        return true;
    }

    private static int checkNotchLocation(List<Logic.MachineXMLParsser.Generated.Rotor> rotorList,int abcSize) {
        for (Logic.MachineXMLParsser.Generated.Rotor r: rotorList) {
            if(r.getNotch()>abcSize){
                return r.getId();
            }
        }
        return 0;
    }

    private static int checkDoubleMapping(List<Logic.MachineXMLParsser.Generated.Rotor> rotorList
    ) {
        for (Logic.MachineXMLParsser.Generated.Rotor r: rotorList) {
            if(isDoubleMapping(r.getMapping())){
                return r.getId();
            }
        }
        return 0;
    }

    private static boolean isDoubleMapping(List<Mapping> mapping) {
        for (Mapping map : mapping) {
            for (Mapping secondMap : mapping) {
                if (!map.equals(secondMap) && map.getTo().equals(secondMap.getTo())) {
                    return false;
                }
                if (!map.equals(secondMap) && map.getFrom().equals(secondMap.getFrom())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkRotorsIdValidation(List<Logic.MachineXMLParsser.Generated.Rotor> rotorList) {
        ArrayList<Integer> rotorsId = new ArrayList<Integer>();
        for (Logic.MachineXMLParsser.Generated.Rotor r: rotorList) {
            if(rotorsId.contains(r.getId())) {
                return false;
            }
            rotorsId.add(r.getId());
        }
        rotorsId.sort(Integer::compareTo);
        if(rotorsId.get(rotorList.size())!=rotorList.size()){
            return false;
        }
        return true;
    }

    private static boolean checkReflectorsId(List<Logic.MachineXMLParsser.Generated.Reflector> reflectors) {
        ArrayList<Integer> refId=new ArrayList<Integer>();
        for (Logic.MachineXMLParsser.Generated.Reflector ref:reflectors) {
            int id = romeToInt(ref.getId());
            if(refId.contains(id)) {
                return false;
            }
            refId.add(id);
        }
        refId.sort(Integer::compareTo);
        if(refId.get(reflectors.size())!=reflectors.size()){
            return false;
        }
        return true;
    }

    public static int romeToInt(String romeDig){
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
}
