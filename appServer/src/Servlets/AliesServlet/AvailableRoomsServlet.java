package Servlets.AliesServlet;


import Ex3.Room.UIRoom;
import Utils.AvailableRoomWithPort;
import Utils.ServerLogic;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AvailableRoomsServlet" ,urlPatterns = "/AvailableRooms")
public class AvailableRoomsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        ServerLogic serverLogic = ServerLogic.getInstance(getServletContext());
        List<UIRoom> rooms = serverLogic.getAvailableRooms();
        String userName = serverLogic.getUsernameFromCookies(req.getCookies());
        int port = serverLogic.getAliesPort(userName);
        resp.getWriter().print(new Gson().toJson(new AvailableRoomWithPort(rooms,port)));
    }
}