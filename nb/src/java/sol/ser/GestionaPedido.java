/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sol.ser;

import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
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
import paw.bd.GestorBDPedidos;
import paw.model.Articulo;
import paw.model.Cliente;
import paw.model.ExcepcionDeAplicacion;
import paw.model.LineaEnRealizacion;
import paw.model.PedidoEnRealizacion;
import paw.util.UtilesString;
import paw.util.servlet.ParameterParser;

/**
 *
 * @author jesus
 */
@WebServlet(name = "GestionaPedido", urlPatterns = {"/clientes/GestionaPedido"})
public class GestionaPedido extends HttpServlet {

    public static GestorBDPedidos gbdp = new GestorBDPedidos();
    public static GestorBD gbd = new GestorBD();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession sesion = req.getSession();
            PedidoEnRealizacion carrito = (PedidoEnRealizacion) sesion.getAttribute("carrito");
            Cliente cliente = (Cliente) sesion.getAttribute("cliente");
            if (carrito == null) {
                carrito = gbdp.getPedidoEnRealizacion(cliente.getCodigo());
                if (carrito == null) {
                    carrito = new PedidoEnRealizacion(cliente);
                }

                sesion.setAttribute("carrito", carrito);

            }

            String accion = req.getParameter("accion");

            if (UtilesString.isVacia(accion)) {
                resp.sendRedirect("Carrito");
            } else if (accion.equals("Comprar")) {
                String ca = req.getParameter("ca");
                if (UtilesString.isVacia(ca)) {
                    req.setAttribute("enlaceSalir", req.getContextPath() + "/BuscarArticulos");
                    resp.sendError(404, "No se ha especificado código de artículo");
                    return;
                } else {
                    Articulo art = gbd.getArticulo(ca);
                    if (art == null) {
                        req.setAttribute("enlaceSalir", req.getContextPath() + "/BuscarArticulos");
                        resp.sendError(404, "El código de artículo proporcionado no es válido");
                        return;
                    } else {
                        //Las siguientes lineas comentadas servirían para incrementar la cantidad de articulos
                        //en la linea del pedido en caso de haber ya un articulo con el mismo codigo en alguna linea
//                        int cantidad = 0;
//                        boolean enLinea = false;
//                        List<LineaEnRealizacion> lineas = carrito.getLineas();
//                        for(LineaEnRealizacion linea : lineas){
//                            if(art.equals(linea.getArticulo())){
//                                linea.setCantidad(linea.getCantidad()+1);
//                                enLinea = true;
//                                break;
//                            }
//                        }
//                        if(!enLinea){
//                            carrito.addLinea(art);
//                        }
                        carrito.addLinea(art);

                        resp.sendRedirect(req.getContextPath() + "/clientes/Carrito");
                        return;
                    }
                }

            } else if (accion.equals("Seguir comprando")) {
                procesaParams(carrito, req);
                resp.sendRedirect(req.getContextPath() + "/BuscarArticulos");

            } else if (accion.equals("Quitar")) {
                String cl = req.getParameter("cl");
                procesaParams(carrito, req);
                carrito.removeLinea(cl);
                resp.sendRedirect(req.getContextPath() + "/clientes/Carrito");
                return;
            } else if (accion.equals("Guardar pedido")) {
                procesaParams(carrito, req);
                gbdp.grabaPedidoEnRealizacion(carrito);
                resp.sendRedirect(req.getContextPath() + "/clientes/AreaCliente");
                return;
            } else if (accion.equals("Cerrar pedido")) {
                procesaParams(carrito, req);
                sesion.setAttribute("carritoACerrar", carrito);

                req.setAttribute("msg", "Se va a proceder a cerrar su pedido en realización. ¿Está usted seguro?");
                req.setAttribute("siLink", "CierraCarrito?accion=cerrar");
                req.setAttribute("noLink", "CierraCarrito?accion=cancelar");

                RequestDispatcher rd = req.getRequestDispatcher("/clientes/confirmacion.jsp");
                rd.forward(req, resp);
                return;
            }
        } catch (ExcepcionDeAplicacion ex) {
            Logger.getLogger(Carrito.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession sesion = req.getSession();
            PedidoEnRealizacion carrito = (PedidoEnRealizacion) sesion.getAttribute("carrito");
            Cliente cliente = (Cliente) sesion.getAttribute("cliente");
            if (carrito == null) {
                carrito = gbdp.getPedidoEnRealizacion(cliente.getCodigo());
                if (carrito == null) {
                    carrito = new PedidoEnRealizacion(cliente);
                }

                sesion.setAttribute("carrito", carrito);

            }

            String accion = req.getParameter("accion");

            if (UtilesString.isVacia(accion)) {
                resp.sendRedirect("Carrito");
            } else if (accion.equals("Comprar")) {
                String ca = req.getParameter("ca");
                if (UtilesString.isVacia(ca)) {
                    req.setAttribute("enlaceSalir", req.getContextPath() + "/BuscarArticulos");
                    resp.sendError(404, "No se ha especificado código de artículo");
                    return;
                } else {
                    Articulo art = gbd.getArticulo(ca);
                    if (art == null) {
                        req.setAttribute("enlaceSalir", req.getContextPath() + "/BuscarArticulos");
                        resp.sendError(404, "El código de artículo proporcionado no es válido");
                        return;
                    } else {
                        //Las siguientes lineas comentadas servirían para incrementar la cantidad de articulos
                        //en la linea del pedido en caso de haber ya un articulo con el mismo codigo en alguna linea
//                        int cantidad = 0;
//                        boolean enLinea = false;
//                        List<LineaEnRealizacion> lineas = carrito.getLineas();
//                        for(LineaEnRealizacion linea : lineas){
//                            if(art.equals(linea.getArticulo())){
//                                linea.setCantidad(linea.getCantidad()+1);
//                                enLinea = true;
//                                break;
//                            }
//                        }
//                        if(!enLinea){
//                            carrito.addLinea(art);
//                        }
                        carrito.addLinea(art);

                        resp.sendRedirect(req.getContextPath() + "/clientes/Carrito");
                        return;
                    }
                }

            } else if (accion.equals("Seguir Comprando")) {
                procesaParams(carrito, req);
                resp.sendRedirect(req.getContextPath() + "/BuscarArticulos");

            } else if (accion.equals("Quitar")) {
                String cl = req.getParameter("cl");
                procesaParams(carrito, req);
                carrito.removeLinea(cl);
                resp.sendRedirect(req.getContextPath() + "/clientes/Carrito");
                return;
            } else if (accion.equals("Guardar pedido")) {
                procesaParams(carrito, req);
                gbdp.grabaPedidoEnRealizacion(carrito);
                resp.sendRedirect(req.getContextPath() + "/clientes/AreaCliente");
                return;
            } else if (accion.equals("Cerrar pedido")) {
                procesaParams(carrito, req);
                sesion.setAttribute("carritoACerrar", carrito);

                req.setAttribute("msg", "Se va a proceder a cerrar su pedido en realización. ¿Está usted seguro?");
                req.setAttribute("siLink", "CierraCarrito?accion=cerrar");
                req.setAttribute("noLink", "CierraCarrito?accion=cancelar");

                RequestDispatcher rd = req.getRequestDispatcher("/clientes/confirmacion.jsp");
                rd.forward(req, resp);
                return;
            }
        } catch (ExcepcionDeAplicacion ex) {
            Logger.getLogger(Carrito.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void procesaParams(PedidoEnRealizacion carrito, HttpServletRequest req) {
        ParameterParser pp = new ParameterParser(req);
        Enumeration<String> pnames = req.getParameterNames();
        while (pnames.hasMoreElements()) {
            String paramName = pnames.nextElement();
            if (paramName.startsWith("C_")) {
                String codLinea = paramName.substring(2);
                LineaEnRealizacion linea = carrito.getLinea(codLinea);
                int cantidad = pp.getIntParameter(paramName, 1);
                linea.setCantidad(cantidad);
            } else if (paramName.startsWith("F_")) {
                String codLinea = paramName.substring(2);
                LineaEnRealizacion linea = carrito.getLinea(codLinea);
                Calendar fe = pp.getCalendarParameter(paramName, "dd/MM/yyyy", Calendar.getInstance());
                linea.setFechaEntregaDeseada(fe);
            }
        }
    }

}
