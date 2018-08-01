package Ex3.Uboat;

import Logic.MachineDescriptor.MachineComponents.Secret;

public interface IUboat {
    ProcessStringReturnValue processString(Secret secret, String strToProcess);
    String getUser();
    void setUser(String mUser);
    void setReady(boolean ready);
    boolean isReady();
}
