package ConsoleUI;

import java.util.List;
import java.util.concurrent.Callable;

public class MenuItem {
    private String stringRepresentation;
    private final Callable<Void> methodToInvokeOnSelected;

    public MenuItem(String i_string, Callable<Void> i_method){
        stringRepresentation = i_string;
        methodToInvokeOnSelected = i_method;
    }
    public void Invoke(){
        try {
            methodToInvokeOnSelected.call();
        } catch (Exception e) {
            throw new RuntimeException("Exception: menuItem >> Invoke: " + e.getMessage());
        }
    }
    public void setString(String string) {
        stringRepresentation = string;
    }
    @Override
    public String toString() {
        return stringRepresentation;
    }
}
