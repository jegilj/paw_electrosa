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
@WebServlet(name = "CierraCarrito", urlPatterns = {"/clientes/CierraCarrito"})
public class CierraCarrito extends HttpServlet {

    public static GestorBDPedidos gbdp = new GestorBDPedidos();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession sesion = req.getSession();
            PedidoEnRealizacion carritoACerrar = (PedidoEnRealizacion) sesion.getAttribute("carritoACerrar");
            if (carritoACerrar == null) {
                req.setAttribute("enlaceSalir", req.getContextPath() + "/clientes/AreaCliente");
                resp.sendError(400, "La aplicación no puede determinar el pedido a cerrar");
                return;
            } else {
                String accion = req.getParameter("accion");
                if (!accion.equals("cerrar")) {
                    sesion.removeAttribute("carritoACerrar");
                    resp.sendRedirect("./Carrito");
                    return;
                } else {
                    Cliente cliente = (Cliente) sesion.getAttribute("cliente");
                    gbdp.cierraPedido(carritoACerrar, cliente.getDireccion());
                    sesion.removeAttribute("carritoACerrar");
                    sesion.removeAttribute("carrito");
                    resp.sendRedirect("./VerPedido");
                    return;
                }
            }
        } catch (ExcepcionDeAplicacion ex) {
            Logger.getLogger(Carrito.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
