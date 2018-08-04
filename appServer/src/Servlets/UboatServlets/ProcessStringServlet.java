package Servlets.UboatServlets;

import AgentDMParts.Secret;
import Ex3.Uboat.ProcessStringReturnValue;
import Logic.MachineDescriptor.MachineComponents.Position;
import Logic.MachineDescriptor.MachineComponents.RotorInSecret;
import Servlets.Response;
import Utils.ServerLogic;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProcessStringServlet" ,urlPatterns = "/ProcessString")
public class ProcessStringServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        //ProcessStringReturnValue res = ServerLogic.getInstance(getServletContext()).processUboatString()
        List<RotorInSecret> rotorInSecrets = new ArrayList<>();
        Position pos = new Position();
        pos.setPositionAsChar('a');
        rotorInSecrets.add(new RotorInSecret(3,pos));
        rotorInSecrets.add(new RotorInSecret(1,pos));
        rotorInSecrets.add(new RotorInSecret(2,pos));
        Secret secret = new Secret(rotorInSecrets,2);
        resp.getWriter().print(new Gson().toJson(secret));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(new Gson().toJson( new Response<String>(true,null)));
    }
}