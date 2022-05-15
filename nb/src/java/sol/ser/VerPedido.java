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
import paw.bd.GestorBDPedidos;
import paw.model.Cliente;
import paw.model.ExcepcionDeAplicacion;
import paw.model.Pedido;
import paw.util.UtilesString;

/**
 *
 * @author jesus
 */
@WebServlet(name = "VerPedido", urlPatterns = {"/clientes/VerPedido"})
public class VerPedido extends HttpServlet {

    private static GestorBDPedidos gbdp = new GestorBDPedidos();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cp = req.getParameter("cp");
        if(UtilesString.isVacia(cp)){
            resp.sendRedirect("PedidosCliente");
            return;
        }
        else{
            try {
                Pedido p = gbdp.getPedido(cp);
                if(p==null){
                    req.setAttribute("enlaceSalir", "PedidosCliente");
                    resp.sendError(404, "Código de pedido "+cp+" inválido");
                    return;
                }
                else{
                    HttpSession sesion = req.getSession();
                    Object clientesesion = sesion.getAttribute("cliente");
                    Cliente cliente = (Cliente) clientesesion;
                    if(!p.getCliente().getCodigo().equals(cliente.getCodigo())){
                        req.setAttribute("enlaceSalir", "../Salir");
                        resp.sendError(403, "Usted no está autorizado para consultar esta información");
                        return;
                    }
                    else{
                        req.setAttribute("pedido", p);
                        RequestDispatcher rd = req.getRequestDispatcher("pedido.jsp");
                        rd.forward(req, resp);
                    }
                    
                }
            } catch (ExcepcionDeAplicacion ex) {
                Logger.getLogger(VerPedido.class.getName()).log(Level.SEVERE, null, ex);
                req.setAttribute("enlaceSalir", "index.html");
                throw new ServletException(ex);
            }
        }
    }
    
            
}
