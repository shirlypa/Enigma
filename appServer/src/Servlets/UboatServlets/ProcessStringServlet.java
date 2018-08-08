package Servlets.UboatServlets;

import AgentDMParts.Secret;
import Ex3.Uboat.ProcessStringReturnValue;
import Logic.Logic;
import Logic.MachineDescriptor.MachineComponents.Position;
import Logic.MachineDescriptor.MachineComponents.RotorInSecret;
import Servlets.Response;
import Utils.ServerLogic;
import com.google.gson.Gson;
import com.sun.security.ntlm.Server;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        ServerLogic serverLogic = ServerLogic.getInstance(getServletContext());
        String randomSecret = req.getParameter("random");
        boolean random = false;
        Secret secret;
        String strToProcess = req.getParameter("str");
        String userName = serverLogic.getUsernameFromCookies(req.getCookies());
        if (userName == null){
            resp.getWriter().println(new Gson().toJson(new Response<String>(false,"no cookies :(")));
            return;
        }
        if (randomSecret != null){
            secret = null;
            random = true;
        } else {
            String secretJson = req.getParameter("secret");
            secret = new Gson().fromJson(secretJson, Secret.class);
            random = false;
        }
        String encodedStr = serverLogic.processUboatString(userName,secret,strToProcess,random);
        resp.getWriter().print(new Gson().toJson( new Response<String>(true,encodedStr)));
    }
}