/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sol.ser;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import paw.model.Cliente;

/**
 *
 * @author jesus
 */
@WebServlet(name = "AreaCliente", urlPatterns = {"/clientes/AreaCliente"})
public class AreaCliente extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        Object clienteSession = sesion.getAttribute("cliente");
        if(clienteSession==null || !(clienteSession instanceof Cliente)){
            response.sendRedirect("../Login");
            return;
        }
        else{
            Cliente cliente = (Cliente) clienteSession;
            request.setAttribute("cliente", cliente);
            RequestDispatcher rd = request.getRequestDispatcher("../clientes/index.jsp");
            rd.forward(request, response);
        }
    }
    
}
