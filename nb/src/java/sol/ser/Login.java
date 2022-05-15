/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sol.ser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import paw.bd.GestorBD;
import paw.model.ExcepcionDeAplicacion;
import paw.util.UtilesString;

/**
 *
 * @author jesus
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

      private static GestorBD gbd = new GestorBD();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String usr = request.getParameter("usr");
        String pwd = request.getParameter("pwd");
        if(UtilesString.isVacia(usr) || UtilesString.isVacia(pwd)){
            request.setAttribute("error", "Algún dato vacío");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
        
        try {
            boolean existe = gbd.comprobarLogin(usr, pwd);
            if(!existe){
                request.setAttribute("error", "Usuario o contraseña incorrecta");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
            }else{
                HttpSession sesion = request.getSession();
                sesion.setAttribute("cliente", gbd.getClienteByUserName(usr));
                response.sendRedirect("clientes/AreaCliente");
            }
        } catch (ExcepcionDeAplicacion ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }
    }
        
        
        
        
    }
    
    


