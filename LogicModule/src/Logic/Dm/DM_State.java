package Logic.Dm;

import AgentDMParts.eDM_State;

public class DM_State {
    private eDM_State state;

    public synchronized eDM_State getState() {
        return state;
    }

    public synchronized void setState(eDM_State state) {
        this.state = state;
    }
}
