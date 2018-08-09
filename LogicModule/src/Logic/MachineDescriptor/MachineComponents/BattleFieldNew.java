package Logic.MachineDescriptor.MachineComponents;

import com.sun.corba.se.spi.activation.Server;

import java.io.Serializable;

public class BattleFieldNew implements Serializable {
    private String level;
    private String battleName;
    private int allies;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBattleName() {
        return battleName;
    }

    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }

    public int getAllies() {
        return allies;
    }

    public void setAllies(int allies) {
        this.allies = allies;
    }
}
