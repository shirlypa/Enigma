package Servlets;

public class Response<T> {
    private boolean success;
    private T payload;
    public Response(boolean i_success, T i_payload){
        success = i_success;
        payload = i_payload;
    }
}

