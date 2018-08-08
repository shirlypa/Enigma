package Servlets.AliesServlet;

import Ex3.update.AliesUpdate;
import Utils.ServerLogic;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AliesUpdateServlet" ,urlPatterns = "/AliesUpdate")
public class AliesUpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        ServerLogic serverLogic = ServerLogic.getInstance(getServletContext());
        String userName = serverLogic.getUsernameFromCookies(req.getCookies());
        AliesUpdate aliesUpdate = serverLogic.getAliesUpdate(userName);
        resp.getWriter().print(new Gson().toJson(aliesUpdate));
    }
}
