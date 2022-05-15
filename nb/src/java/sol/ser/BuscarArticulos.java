/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sol.ser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import paw.bd.CriteriosArticulo;
import paw.bd.GestorBD;
import paw.bd.Paginador;
import paw.model.Articulo;
import paw.model.ExcepcionDeAplicacion;
import paw.util.UtilesString;
import paw.util.servlet.ParameterParser;
import paw.util.servlet.UtilesServlet;

/**
 *
 * @author jesus
 */
public class BuscarArticulos extends HttpServlet {
    
  private static GestorBD gbd = new GestorBD();
    
  private static int tamanioPagina = 15;

  @Override
  public void init() throws ServletException {
    super.init();
    try {
      tamanioPagina = Integer.parseInt(this.getInitParameter("tamanioPagina"));
    } catch (Exception ex) {
        Logger.getLogger(BuscarArticulos.class.getName()).log(Level.WARNING, null, ex);
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
       
    ParameterParser pp = new ParameterParser(request);
    try {
      CriteriosArticulo criterios = (CriteriosArticulo) UtilesServlet.populateBean(CriteriosArticulo.class, request);

      if (!UtilesString.isVacia(criterios.getCodigo()) || 
              !UtilesString.isVacia(criterios.getFabricante()) ||
              !UtilesString.isVacia(criterios.getNombre()) || 
              !UtilesString.isVacia(criterios.getPrecio()) || 
              !UtilesString.isVacia(criterios.getTipo())) {
          
        Paginador paginador = gbd.getPaginadorArticulos(criterios, tamanioPagina);
        
        String direccion = "BuscarArticulos?"
                + "tipo=" + (criterios.getTipo() != null ? criterios.getTipo() : "")
                + "&fabricante=" + (criterios.getFabricante() != null ? criterios.getFabricante() : "")
                + "&precio=" + (criterios.getPrecio() != null ? criterios.getPrecio() : "")
                + "&codigo=" + (criterios.getCodigo() != null ? criterios.getCodigo() : "")
                + "&nombre=" + (criterios.getNombre() != null ? criterios.getNombre() : "")
                + "&p=";

        int paginaActual = pp.getIntParameter("p", 1);
        
        if (paginaActual < 1) {
          response.sendRedirect(direccion + "1");
          return;
        }
        if (paginador.getNumPaginas() > 0 && paginaActual > paginador.getNumPaginas()
                || paginador.getNumPaginas() == 0 && paginaActual > 1 ) {
          response.sendRedirect(direccion + paginador.getNumPaginas());
          return;
        }
        List<Articulo> articulos = gbd.getArticulos(criterios, paginaActual, tamanioPagina);
        
        
        request.setAttribute("articulos", articulos);
        request.setAttribute("direccion", direccion);
        request.setAttribute("paginador", paginador);
        request.setAttribute("paginaActual", paginaActual);
        request.setAttribute("numRegs", paginador.getNumRegistros());
        request.setAttribute("numPags", paginador.getNumPaginas());
        request.setAttribute("tipo", criterios.getTipo());
        request.setAttribute("fabricante", criterios.getFabricante());
        request.setAttribute("precio", criterios.getPrecio());
      }
      request.setAttribute("tiposArticulos", gbd.getTiposArticulos());
      request.setAttribute("fabricantes", gbd.getFabricantes());
      
        RequestDispatcher rd = request.getRequestDispatcher("/catalogo.jsp");
        rd.forward(request, response);
    } catch (ExcepcionDeAplicacion ex) {
      Logger.getLogger(BuscarArticulos.class.getName()).log(Level.SEVERE, null, ex);
      throw new ServletException(ex);
    }
       
  }



}
