package Servlets.UboatServlets;

import Logic.MachineDescriptor.MachineComponents.Position;
import Logic.MachineDescriptor.MachineComponents.Rotor;
import Logic.MachineDescriptor.MachineComponents.RotorInSecret;
import Logic.MachineDescriptor.MachineDescriptor;
import Logic.MachineXMLParsser.*;
import Logic.MachineXMLParsser.Generated.Battlefield;
import Servlets.Response;
import Utils.ServerLogic;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@WebServlet(name = "UploadXMLServlet" ,urlPatterns = "/uploadXml")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadXMLServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Collection<Part> parts = req.getParts();

        String userName = ServerLogic.getInstance(getServletContext()).getUsernameFromCookies(req.getCookies());
        if (userName == null){
            resp.getWriter().println(new Gson().toJson(new Response<String>(false,"no cookies :(")));
            return;
        }

        Cookie cookies [] = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("userName"))
                    userName = c.getValue();
            }
            System.out.println("userName:" + userName);
        }

        StringBuilder fileContent = new StringBuilder();
        System.out.println(fileContent.toString());

        for (Part part : parts) {
            fileContent.append(readFromInputStream(part.getInputStream()));
        }


        MachineDescriptor machineDescriptor = null;
        List<String> validErrors = new ArrayList<>();
        try {
            machineDescriptor = MachineXMLParsser.parseMachineFromXMLContent(fileContent.toString());
        } catch (DoubleMappingException e) {
            validErrors.add("One of the rotors Mapping the same character twice or more");
        } catch (InvalidNotchLocationException e) {
            validErrors.add("Notch can't be bigger then rotor size");
        } catch (InvalidReflectorMappingException e) {
            validErrors.add("One of the reflectors reflect the same character");
        } catch (InvalidRotorsIdException e) {
            validErrors.add("Each rotor must have unique id, start from 1");
        } catch (InvalidReflectorIdException e) {
            validErrors.add("Each reflector must have unique id (I-V)");
        } catch (AlphabetIsOddException e) {
            validErrors.add("Alphabet length must be even");
        } catch (InvalidRotorsCountException e) {
            validErrors.add("Rotors count can't be bigger than total amount of rotors, and can't be smaller than 2");
        } catch (InvalidAgentsNumberException e) {
            //
        }
        if (validErrors.size() > 0){
            resp.getWriter().print(new Gson().toJson(new Response<List<String>>(false,validErrors)));
            return;
        }
        //TODO check xml validation.
        ServerLogic.getInstance(getServletContext()).setUboatXml(userName,machineDescriptor);
        resp.getWriter().print(new Gson().toJson(new Response<String>(true,null)));
    }
    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
