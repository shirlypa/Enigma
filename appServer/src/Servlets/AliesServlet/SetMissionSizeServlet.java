package Servlets.AliesServlet;

import Utils.ServerLogic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SetMissionSizeServlet" ,urlPatterns = "/SetMissionSize")
public class SetMissionSizeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        ServerLogic serverLogic = ServerLogic.getInstance(getServletContext());
        String userName = serverLogic.getUsernameFromCookies(req.getCookies());
        String missionSizeStr =  req.getParameter("missionSize");
        int missionSize = Integer.parseInt(missionSizeStr);
        serverLogic.setAliesMissionSize(userName,missionSize);
        resp.getWriter().print(true);
    }
}
