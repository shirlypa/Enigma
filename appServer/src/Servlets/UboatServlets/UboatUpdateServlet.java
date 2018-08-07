package Servlets.UboatServlets;

import Ex3.update.UboatUpdate;
import Utils.ServerLogic;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UboatUpdateServlet" ,urlPatterns = "/UboatUpdate")
public class UboatUpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        ServerLogic serverLogic = ServerLogic.getInstance(getServletContext());
        String userName = serverLogic.getUsernameFromCookies(req.getCookies());
        UboatUpdate res = serverLogic.getUboatUpdate(userName);
        resp.getWriter().print(new Gson().toJson(res));
    }
}
