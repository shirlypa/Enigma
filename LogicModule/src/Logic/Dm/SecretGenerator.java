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


    public SecretGenerator(eProccessLevel proccessLevel, int rotorsInUserNumber) {
        this.proccessLevel = proccessLevel;

        currentRotorsInUse = new int[rotorsInUserNumber];
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
            rotorInSecretList.add(new RotorInSecret(currentRotorsInUse[i],new Position().setPositionAsInt(0)));
        }
        return new Secret(rotorInSecretList,currentReflectorInUse);
    }

    private void setNewRotorsAndOrder() {
        //TODO
    }

    private void setNewRotorsOrder() {
        //TODO
    }

    public void setCurrentRotorsInUse(int[] currentRotorsInUse) {
        this.currentRotorsInUse = currentRotorsInUse;
    }

    public void setCurrentReflectorInUse(int currentReflectorInUse) {
        this.currentReflectorInUse = currentReflectorInUse;
    }

    public Secret getInitialSecret(){
        List<RotorInSecret> rotorInSecretList = new ArrayList<>();
        for (int i = 0; i < currentRotorsInUse.length; i++) {
            rotorInSecretList.add(new RotorInSecret(currentRotorsInUse[i],new Position().setPositionAsInt(0)));
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