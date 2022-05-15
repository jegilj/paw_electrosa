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
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import paw.bd.GestorBD;
import paw.model.Cliente;
import paw.model.Direccion;
import paw.model.ExcepcionDeAplicacion;
import paw.util.UtilesString;
import paw.util.Validacion;
import paw.util.mail.DatosCorreo;
import paw.util.mail.GestorCorreo;
import paw.util.mail.conf.ConfiguracionCorreo;
import paw.util.servlet.UtilesServlet;

/**
 *
 * @author jesus
 */
@WebServlet(name = "NuevoCliente", urlPatterns = {"/NuevoCliente"})
public class NuevoCliente extends HttpServlet {

    public static GestorBD gbd = new GestorBD();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("nuevoCliente.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            Cliente cliente = (Cliente) UtilesServlet.populateBean("paw.model.Cliente", request);
            Direccion direccion = (Direccion) UtilesServlet.populateBean("paw.model.Direccion", request);
            cliente.setDireccion(direccion);
            int privacidadOK = 0;
            if (request.getParameter("privacidad") != null && request.getParameter("privacidad").equals("1")) {
                privacidadOK = 1;
            }

            List<String> listaErrores = this.valida(cliente, request.getParameter("login"), request.getParameter("pwd"), request.getParameter("rpwd"), privacidadOK, gbd);
            if (!listaErrores.isEmpty()) {
                request.setAttribute("cliente", cliente);
                request.setAttribute("direccion", direccion);
                request.setAttribute("listaErrores", listaErrores);
                RequestDispatcher rd = request.getRequestDispatcher("nuevoCliente.jsp");
                rd.forward(request, response);
            } else {
                Cliente clienteCod = gbd.insertaCliente(cliente, request.getParameter("login"), request.getParameter("pwd"));
                HttpSession sesion = request.getSession();
                sesion.setAttribute("cliente", clienteCod);

                //Enviamos correo de confirmacion:
                if (clienteCod != null) { //Se ha creado el cliente correctamente
                    String server = request.getServerName();
                    String port = String.valueOf(request.getServerPort());
                    String context = request.getContextPath();

                    String url = "http://" + server + ":" + port + context;
                    String to = request.getParameter("email");
                    DatosCorreo mens = new DatosCorreo("jegilj@unirioja.es", to, "Bienvenido a Electrosa",
                            "Bienvenido a electrosa.com \n"
                            + "Es un placer para nosotros tenerle como cliente. Visite nuestra web en la dirección:\n"
                            + url);
                    mens.setMimeType("text/plain;charset=UTF-8");
                    mens.setCharset("UTF-8");
                    GestorCorreo.envia(mens, ConfiguracionCorreo.getDefault());

                }
                response.sendRedirect("clientes/AreaCliente");
            }
        } catch (ExcepcionDeAplicacion ex) {
            Logger.getLogger(NuevoCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        } catch (AddressException ex) {
            Logger.getLogger(NuevoCliente.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(NuevoCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        } catch (MessagingException ex) {
            Logger.getLogger(NuevoCliente.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(NuevoCliente.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Realiza las validaciones para los campos del formulario de registro de
     * nuevo cliente
     *
     * @param cli objeto paw.model.Cliente con los datos leídos del formulario
     * @param usr valor del campo "nombre de usuario" del formulario
     * @param pwd valor del campo "contraseña" del formulario
     * @param rpwd valor del campo "Repita contraseña" del formulario
     * @param privacidadOK debe tener valor 1 si la casilla de "Política de
     * privacidad" está marcada
     * @param gbd objeto GestorBD para ser usado en las comprobaciones que
     * requieran de conexión a al BD
     * @return Una lista de String con mensajes de error correspondientes a las
     * reglas de validación que no se cumplen
     * @throws ExcepcionDeAplicacion
     */
    private List<String> valida(Cliente cli,
            String usr,
            String pwd,
            String rpwd,
            int privacidadOK,
            GestorBD gbd)
            throws ExcepcionDeAplicacion {

        List<String> errores = new ArrayList<String>();

        if (UtilesString.isVacia(cli.getNombre())
                || UtilesString.isVacia(cli.getCif())
                || UtilesString.isVacia(cli.getDireccion().getCalle())
                || UtilesString.isVacia(cli.getDireccion().getCiudad())
                || UtilesString.isVacia(cli.getDireccion().getProvincia())
                || UtilesString.isVacia(cli.getDireccion().getCp())
                || UtilesString.isVacia(cli.getEmail())
                || UtilesString.isVacia(usr)
                || UtilesString.isVacia(pwd)
                || UtilesString.isVacia(rpwd)) {
            errores.add("Debes proporcionar valor para todos los campos requeridos");
        }

        if (cli.getNombre() != null && cli.getNombre().length() > 50) {
            errores.add("La longitud máxima del nombre son 50 caracteres");
        }

        if (cli.getCif() != null && cli.getCif().length() > 12) {
            errores.add("La longitud máxima del CIF son 12 caracteres");
        }

        if (cli.getTfno() != null && cli.getTfno().length() > 11) {
            errores.add("La longitud máxima del teléfono son 11 caracteres");
        }

        if (cli.getEmail() != null && cli.getEmail().length() > 100) {
            errores.add("La longitud máxima del email son 100 caracteres");
        }

        if (usr != null && usr.length() > 50) {
            errores.add("La longitud máxima del userName son 50 caracteres");
        }

        if (cli.getDireccion().getCalle() != null && cli.getDireccion().getCalle().length() > 50) {
            errores.add("La longitud máxima de la calle son 50 caracteres");
        }

        if (cli.getDireccion().getCiudad() != null && cli.getDireccion().getCiudad().length() > 20) {
            errores.add("La longitud máxima de la ciudad son 20 caracteres");
        }

        if (privacidadOK != 1) {
            errores.add("Debes leer la cláusula de privacidad y marcar la casilla correspondiente");
        }

        if (!UtilesString.isVacia(pwd) && !UtilesString.isVacia(rpwd) && !pwd.equals(rpwd)) {
            errores.add("Las contraseñas son diferentes");
        }

        if (!UtilesString.isVacia(usr) && usr.contains(" ")) {
            errores.add("El nombre de usuario tiene espacios en blanco");
        }

        if (!UtilesString.isVacia(usr) && gbd.getClienteByUserName(usr) != null) {
            errores.add("Ya existe un usuario en la BD con ese nombre de usuario");
        }

        if (!UtilesString.isVacia(cli.getCif()) && gbd.getClienteByCIF(cli.getCif()) != null) {
            errores.add("Ya existe un usuario en la BD con ese CIF");
        }

        if (!UtilesString.isVacia(cli.getEmail()) && !Validacion.isEmailValido(cli.getEmail())) {
            errores.add("El email no parece una dirección de correo válida");
        }

        if (!UtilesString.isVacia(cli.getDireccion().getCp()) && !Validacion.isCPValido(cli.getDireccion().getCp())) {
            errores.add("El CP no parece un código postal válido");
        }

        return errores;
    }
}
