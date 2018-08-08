package Servlets;

import Utils.Consts;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LogoutServlet" ,urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String userName = req.getParameter("username");
        List<String> activeUsers = (List<String>)getServletContext().getAttribute(Consts.ActiveUsers);
        activeUsers.remove(userName);
        resp.getWriter().print(true);
    }
}
