package Servlets.UboatServlets;

import Ex3.Room.Room;
import Logic.MachineDescriptor.MachineDescriptor;
import Utils.ServerLogic;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MachineDescriptorServlet" ,urlPatterns = "/MachineDescriptor")
public class MachineDescriptorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String roomName = req.getParameter("roomName");
        Room room = ServerLogic.getInstance(getServletContext()).getRoom(roomName);
        MachineDescriptor machineDescriptor = room.getMachineDescriptor();
        Gson gson = new Gson();
        resp.getWriter().print(gson.toJson(machineDescriptor));
    }
}
