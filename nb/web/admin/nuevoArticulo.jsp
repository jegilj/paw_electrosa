<%-- 
    Document   : nuevoArticulo
    Created on : 13-may-2022, 18:00:41
    Author     : jesus
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" lang="es-ES">
    <title>Nuevo art&iacute;culo</title>
    <meta name="description" content="Aplicación de prueba de Programación de aplicaciones Web; Grado en informática; Universidad de La Rioja." lang="es-ES">
    <meta name="keywords" content="electrodomesticos" lang="es-ES">
    <meta name="language" content="es-ES">
    <meta name="robots" content="index,follow">
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <link href="${ctx}/css/electrosa.css" rel="stylesheet" media="all" type="text/css">
    <link href="${ctx}/css/formulario.css" rel="stylesheet" media="all" type="text/css">
    <link href="${ctx}/css/clientes.css" rel="stylesheet" media="all" type="text/css">
    <link href="${ctx}/css/estilomenu.css" rel="stylesheet" media="all" type="text/css" />
  </head>

  <body >
    <div class="logo"><a href="${ctx}/index.html"><img src="${ctx}/img/LogoElectrosa200.png" border="0"></a></div>


    <div class="sombra">
      <div class="nucleo">
        <div id="lang">
          <a href="${ctx}/index.html">Español</a> &nbsp; | &nbsp; <a href="${ctx}/index.html">English</a>
        </div>
      </div>
    </div>  

    <div class="barra_menus">
      <div class="pestanias">
        <div class="grupoPestanias">
          <div class="pestaniaNoSel"><a href="${ctx}/index.html">Para usuarios</a></div>
          <div class="pestaniaSel">Intranet</div>
        </div>
      </div>

      <div id="cssmenu">
        <ul>
          <li class='has-sub'><a href="${ctx}/index.html">Art&iacute;culos</a>
            <ul>
              <li><a href="${ctx}/admin/listadoArticulos.jsp">Listar</a></li>
              <li><a href="${ctx}/admin/NuevoArticulo">A&ntilde;adir</a> </li>
              <li><a href="${ctx}/HazEstadistica">Estadísticas</a> </li>
            </ul>
          </li>
          <li class='has-sub'><a href="${ctx}/index.html">Clientes</a>	
            <ul>
              <li><a href="${ctx}/index.html">Listar</a> </li>
              <li><a href="${ctx}/index.html">A&ntilde;adir</a> </li>
            </ul>    
          </li>
          <li><a href="${ctx}/index.html">Pedidos</a></li>
          <li class='last'><a href="${ctx}/index.html">Stocks</a></li>
        </ul>
        <div style="clear: left;"></div>
      </div>
    </div> 

    <div class="sombra">
      <div class="nucleo">
        <div id="migas">
          <a href="${ctx}/index.html" title="Inicio" >Inicio</a>
        </div>

        <div class="clear"></div>

        <div class="contenido">

          <h1>A&ntilde;adir un art&iacute;culo    </h1>
          <p>Utilice el siguiente formulario.   </p>

         <c:if test="${!empty errores}">
                <div class="alerta">
                    <c:forEach var="e" items="${errores}">
                        ${e}<br>
                    </c:forEach>
                </div> 
            </c:if>    

          <form id="fArtic" name="fArtic" action="" method="" enctype="">
            <fieldset> 
              <legend>Datos del art&iacute;culo </legend> 
              <div class="field">
                <label for="nombre">Nombre:
                 <c:if test="${empty articulo.nombre}">  
                        <span class='Requerido'>Requerido</span>
                    </c:if>
                </label>
                <!--<input type="text" name="nombre" id="nombre" size="55" value="${(!empty articulo.getNombre())? articulo.getNombre() : ''}"/>-->
                <input type="text" name="nombre" id="nombre" size="55" value="${(!empty articulo.getNombre())? articulo.getNombre() : ''}"/>
              </div>
              <div class="field">
                <label for="pvp">P.V.P:
                  <c:if test="${empty articulo.pvp || articulo.pvp <= 0}">
                        <span class='Requerido'>Requerido</span>
                    </c:if>
                </label>
                <input type="text" name="pvp" id="pvp" size="15" value="${(!empty articulo.getPvp())? articulo.getPvp() : ''}"/>
              </div>
              <div>
                <div class="left">
                  <div class="field">
                    <label for="tipo">Tipo:
                       <c:if test="${empty articulo.tipo}">
                            <span class='Requerido'>Requerido</span>
                        </c:if>
                    </label>
                    <select name="tipo" id="tipo">
                                <option value="-1" disabled selected>- Elige -</option>
                                <c:forEach var="t" items="${tiposArticulos}">
                                    <option value="${t}" ${(t eq tipo ? ' selected' : '')} >${t}</option>
                                </c:forEach>
                            </select>      
                    <input id="otroTip" type="checkbox" name="" value="" title="Introduce otro tipo" disabled/>               
                    Otro 
                    <div id="otrotipoCont"><!--<label>&nbsp;</label><input class="text" type="text" name="tipo" id="tipo" value="" >--></div>
                  </div>
                </div>
                <div class="right">
                  <div class="field">
                    <label for="fabricante">Fabricante:
                      <span class='Requerido'>Requerido</span>
                    </label>
                    <select name="fabricante" id="fabricante">
                                <option value="-1" disabled selected>- Elige -</option>
                                <c:forEach var="f" items="${fabricantes}">
                                    <option value="${f}" ${(f eq fabricante ? ' selected':'')}>${f}</option>
                                </c:forEach>
                            </select>
                    <input id="otroFab" type="checkbox" name="" value=""  title="Introduce otro fabricante" disabled/>
                    Otro 
                    <div id="otrofabricanteCont"><!--<label>&nbsp;</label><input class="text" type="text" name="fabricante" id="fabricante" value="" >--></div>
                  </div>
                </div>		
              </div>

              <div class="field">
                <label for="descripcion">
                  Descripci&oacute;n:
                </label>
                <textarea name="descripcion" id="descripcion" cols="70" rows="3"></textarea>
              </div>
              
<style>
  .fotoBtn {
    width: auto;
    background:#5C7E72;
    padding:8px 10px 10px 10px;
    color:#FFF;
  }
</style>              

              <div class="field">
                <label for="fichFoto" class="fotoBtn">
                  Fichero fotograf&iacute;a
                </label>
                <input type="file" name="fichFoto" id="fichFoto" style="display:none">
              </div>


            </fieldset>


            <fieldset class="submit"> 
              <div class="right">
                <div class="field">
                  <input class="submitb" type="submit"  value="Enviar los datos" >  
                </div>
              </div>
            </fieldset>  
          </form>

        </div>
      </div>

      <div class="separa"></div>

      <div class="pie">
        <span class="pie_izda">
          <a href="mailto:francisco.garcia@unirioja.es">Contacto</a> &nbsp; | &nbsp; <a href="../mapa.html">Mapa</a> </span>
        <span class="pie_dcha">
          &copy; 2012 Francisco J. García Izquierdo  </span>
        <div class="clear"></div>  
      </div>

    </div>
  </body>
</html>