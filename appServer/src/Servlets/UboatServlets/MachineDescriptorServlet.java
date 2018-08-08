package Servlets.UboatServlets;

import Ex3.Room.Room;
import AgentDMParts.MachineDescriptor;
import Utils.ServerLogic;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MachineDescriptorServlet" ,urlPatterns = "/MachineDescriptor")
public class MachineDescriptorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        ServerLogic serverLogic = ServerLogic.getInstance(getServletContext());
        String userName = serverLogic.getUsernameFromCookies(req.getCookies());
        Room room = serverLogic.getRoomByUsername(userName);
        MachineDescriptor machineDescriptor = room.getMachineDescriptor();
        Gson gson = new Gson();
        resp.getWriter().print(gson.toJson(machineDescriptor));
    }
}
