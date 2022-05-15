/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sol.ser;

import java.io.IOException;
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
import paw.model.PedidoEnRealizacion;



/**
 *
 * @author jesus
 */
@WebServlet(name = "Carrito", urlPatterns = {"/clientes/Carrito"})
public class Carrito extends HttpServlet {

    private static GestorBDPedidos gbdp = new GestorBDPedidos();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object clienteSesion = session.getAttribute("cliente");
        Cliente cliente = (Cliente) clienteSesion;
        Object perSesion =  session.getAttribute("carrito");
        PedidoEnRealizacion carrito = (PedidoEnRealizacion) perSesion;
        if (carrito == null) {
            try {
                carrito = gbdp.getPedidoEnRealizacion(cliente.getCodigo());
                session.setAttribute("carrito", carrito);
            } catch (ExcepcionDeAplicacion ex) {
                Logger.getLogger(Carrito.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        RequestDispatcher rd = request.getRequestDispatcher("carrito.jsp");
        rd.forward(request, response);
    }


}
