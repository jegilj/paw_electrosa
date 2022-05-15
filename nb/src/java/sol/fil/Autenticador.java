/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sol.fil;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import paw.bd.GestorBD;
import paw.model.Cliente;

/**
 *
 * @author jesus
 */
@WebFilter(filterName = "Autenticador", urlPatterns = {"/clientes/*"})
public class Autenticador implements Filter {

    public static GestorBD gbd = new GestorBD();

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession sesion = req.getSession();
        Object clienteSesion = sesion.getAttribute("cliente");
        Cliente cliente = (Cliente) clienteSesion;
        if (cliente == null) {
            String returnURL = req.getRequestURL().toString();
            if (req.getQueryString() != null) {
                returnURL = returnURL + "?" + req.getQueryString();
            }
            sesion.setAttribute(returnURL, "returnURL");
            res.sendRedirect(req.getContextPath() + "/Login");
            return;
        }
        {
            chain.doFilter(request, response);
        }

    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {

    }
}
