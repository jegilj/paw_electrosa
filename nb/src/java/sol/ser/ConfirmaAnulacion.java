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

/**
 *
 * @author jesus
 */
@WebServlet(name = "ConfirmaAnulacion", urlPatterns = {"/clientes/ConfirmaAnulacion"})
public class ConfirmaAnulacion extends HttpServlet {
    
    private static GestorBDPedidos gbdp = new GestorBDPedidos();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String cp = req.getParameter("cp");
            Pedido pedido = gbdp.getPedido(cp);
            HttpSession sesion = req.getSession();
            Cliente cliente = (Cliente) sesion.getAttribute("cliente");
            if(pedido!=null){
                Boolean corresponde = false;
                for(Pedido p: gbdp.getPedidos(cliente.getCodigo())){
                    if(p.getCodigo().equals(pedido.getCodigo())){
                        corresponde= true;
                    }
                    if(corresponde){
                        if(!pedido.isCursado()){
                            gbdp.anulaPedido(pedido);
                            RequestDispatcher rd = req.getRequestDispatcher("pedidosCliente.jsp");
                            rd.forward(req, resp);
                        }
                        else{
                            req.setAttribute("enlaceSalir", "../Salir");
                        resp.sendError(400, "El pedido está cursado, no puede anularlo");
                        return;
                        }
                    }
                    else{
                        req.setAttribute("enlaceSalir", "../Salir");
                        resp.sendError(403, "No tiene permiso para anular este pedido");
                        return;
                    }
                }
                
            }
        }
        catch(ExcepcionDeAplicacion ex){
            Logger.getLogger(NuevoCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
            
        }
    }

}
