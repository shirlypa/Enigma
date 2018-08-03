package Servlets;

import Ex3.Room.Room;
import Utils.Consts;
import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "SignInServlet" ,urlPatterns = "/signin")
public class SignInServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean k_validUserName = true;
        response.setContentType("application/json;charset=UTF-8");
        String userName = request.getParameter("username");
        String userType = request.getParameter("userType");

        List<Room> rooms = (List<Room>)getServletContext().getAttribute(Consts.Rooms);
        if (rooms == null){
            rooms = new ArrayList<>();
            getServletContext().setAttribute(Consts.Rooms,rooms);
        }
        if(isUserExist(rooms,userName)){
            response.getWriter().print(!k_validUserName);
        }

    }
}
