/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sol.ser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
import paw.model.PedidoAnulado;
import paw.model.Pedido;
import paw.util.servlet.UtilesServlet;

/**
 *
 * @author jesus
 */
@WebServlet(name = "PedidosCliente", urlPatterns = {"/clientes/PedidosCliente"})
public class PedidosCliente extends HttpServlet {
    public static GestorBDPedidos gbdped = new GestorBDPedidos();
    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            HttpSession sesion = req.getSession();
            Object clienteSession = sesion.getAttribute("cliente");
            Cliente cliente = (Cliente) clienteSession;
            List<Pedido> pp = gbdped.getPedidosPendientes(cliente.getCodigo());
            List<Pedido> pc = gbdped.getPedidosCompletados(cliente.getCodigo());
            List<PedidoAnulado> pa = gbdped.getPedidosAnulados(cliente.getCodigo());
            req.setAttribute("pedidospendientes",pp);
            req.setAttribute("pedidoscompletados",pc);
            req.setAttribute("pedidosanulados", pa);
            RequestDispatcher rd = req.getRequestDispatcher("pedidosCliente.jsp");
            rd.forward(req, resp);
        }
         catch (ExcepcionDeAplicacion ex) {
            Logger.getLogger(PedidosCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
