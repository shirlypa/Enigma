package Servlets.AliesServlet;

import Utils.ServerLogic;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EnterRoomServlet" ,urlPatterns = "/EnterRoom")
public class EnterRoomServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        ServerLogic serverLogic = ServerLogic.getInstance(getServletContext());
        String userName =  serverLogic.getUsernameFromCookies(req.getCookies());
        String roomName = req.getParameter("roomName");
        boolean res = serverLogic.linkAlliesToRoom(userName,roomName);
        resp.getWriter().print(res);
    }
}
