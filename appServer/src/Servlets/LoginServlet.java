package Servlets;

import Ex3.Room.Room;
import Ex3.update.ePlayerType;
import Utils.Consts;
import Utils.ServerLogic;
import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "LoginServlet" ,urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String userName = request.getParameter("userName");
        String userType = request.getParameter("userType");
        ePlayerType playerType;
        if (userType.equals(Consts.UBOAT)){
            playerType = ePlayerType.Uboat;
        } else {
            playerType = ePlayerType.Alies;
        }

        boolean res = ServerLogic.getInstance(getServletContext()).login(userName,playerType);
        if (res){
            Cookie cookie = new Cookie("userName",userName);
            cookie.setMaxAge(60*60*24);
            response.addCookie(cookie);
        }
        response.getWriter().print(res);
    }
}
