package Logic.Dm;

import Logic.MachineDescriptor.MachineComponents.Position;
import Logic.MachineDescriptor.MachineComponents.RotorInSecret;
import Logic.MachineDescriptor.MachineComponents.Secret;

import java.util.ArrayList;
import java.util.List;

public class SecretGenerator {
    private eProccessLevel proccessLevel;
    private int[] currentRotorsInUse; //and their order
    private int currentReflectorInUse;
    private boolean isRotorsOrdered;
    private String alphabet;


    public SecretGenerator(eProccessLevel proccessLevel, int rotorsInUserNumber, String alphabet) {
        this.proccessLevel = proccessLevel;

        currentRotorsInUse = new int[rotorsInUserNumber];
        this.alphabet = alphabet;
        for (int i = 0; i < currentRotorsInUse.length; i++) {
            currentRotorsInUse[i] = i+1;
        }
        currentReflectorInUse = 1;
        isRotorsOrdered = false;
    }

    public Secret advanceRotorsAndReflectorByLevel(){
        switch (proccessLevel) {
            case EASY:
                return null; //on easy should not get here
            case MEDIUM: //only reflector not known
                currentReflectorInUse++;
                break;
            case HARD: //reflector and Rotors order not known
                setNewRotorsOrder();
                break;
            case IMPOSSIBLE://reflectors, rotors, and rotors order not known
                setNewRotorsAndOrder();
                break;
        }
        return createNewSecret();
    }

    private Secret createNewSecret() {
        List<RotorInSecret> rotorInSecretList = new ArrayList<>();
        for (int i = 0; i < currentRotorsInUse.length; i++) {
            RotorInSecret rotor = new RotorInSecret(currentRotorsInUse[i],new Position().setPositionAsInt(0));
            rotor.getPosition().setPositionAsChar(alphabet.charAt(0));
            rotorInSecretList.add(rotor);
        }
        return new Secret(rotorInSecretList,currentReflectorInUse);
    }

    // advance reflectors, choose rotors and the order
    private void setNewRotorsAndOrder() {
        //TODO
        currentReflectorInUse++;
    }

    // advance reflectors, the order of rotors
    private void setNewRotorsOrder() {
        //TODO
        currentReflectorInUse++;


    }

    //TODO move the secret advanced to here


    public void setCurrentRotorsInUse(int[] currentRotorsInUse) {
        this.currentRotorsInUse = currentRotorsInUse;
    }

    public void setCurrentReflectorInUse(int currentReflectorInUse) {
        this.currentReflectorInUse = currentReflectorInUse;
    }

    public Secret getInitialSecret(){
        List<RotorInSecret> rotorInSecretList = new ArrayList<>();
        for (int i = 0; i < currentRotorsInUse.length; i++) {
            rotorInSecretList.add(new RotorInSecret(currentRotorsInUse[i],new Position().setPositionAsInt(1).setPositionAsChar(alphabet.charAt(0))));
        }
        return new Secret(rotorInSecretList,this.currentReflectorInUse);
    }


    public boolean isRotorsOrdered() {
        return isRotorsOrdered;
    }

    public void setRotorsOrdered(boolean rotorsOrdered) {
        isRotorsOrdered = rotorsOrdered;
    }
}
