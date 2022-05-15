<%-- 
    Document   : carrito
    Created on : 02-may-2022, 18:40:28
    Author     : jesus
--%>


<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" lang="es-ES">
        <title>Electrosa >> Pedido en realización</title>
        <meta name="description" content="Aplicación de prueba de Programación de aplicaciones Web; Grado en informática; Universidad de La Rioja." lang="es-ES">
        <meta name="keywords" content="electrodomesticos" lang="es-ES">
        <meta name="language" content="es-ES">
        <meta name="robots" content="index,follow">

        <link href="../css/electrosa.css" rel="stylesheet" media="all" type="text/css">
        <link href="../css/clientes.css" rel="stylesheet" media="all" type="text/css">
        <link href="../css/pedidoR.css" rel="stylesheet" media="all" type="text/css">
    </head>

    <body >

        <%@include file="cabecera.html" %>

        <div class="sombra">
            <div class="nucleo">
                <div id="migas">
                    <a href="../index.html" title="Inicio" >Inicio</a> &nbsp; | &nbsp; 
                    <a href="AreaCliente" title="Área de cliente">Área de cliente</a>
                </div>
                <div id="cliente">
                    Bienvenido, ${cliente.nombre}
                </div>
                <div class="clear"></div>
                <div class="contenido">
                    <h1>Contenido de su  pedido    </h1>
                    <c:if test="${ carrito == null}">
                        <p>Usted no tiene pedidos</p>
                    </c:if>




                    <c:if test="${ carrito != null}">
                        <p>Pedido iniciado el <fmt:formatDate pattern="dd/MM/yyyy" value="${carrito.inicio.time}"/> a las  <fmt:formatDate pattern="HH:mm" type="time" value="${carrito.inicio.time}"/>.</p>
                        <form action="GestionaPedido" method="post">
                            <table width="95%" cellspacing="0">
                                <tr>
                                    <td colspan="2"><img src="../img/AddCartb.png" style="vertical-align:middle;margin-bottom:3px; margin-left:15px">&nbsp; Art&iacute;culos del pedido</td>
                                    <td width="10%">P.V.P.</td>
                                    <td width="10%">Cantidad</td>
                                    <td width="16%">Fecha de entrega (dd/mm/yyyy)</td>
                                </tr>
                                <c:forEach var="linea" items="${carrito.lineas}">
                                    <tr >
                                        
                                        <td width="6%" style="text-align:center"><a href="GestionaPedido?accion=Quitar&cl=${linea.codigo}"><img src="../img/cancel.png" alt="Quitar del pedido" border="0" title="Quitar del pedido"></td>
                                        <td width="58%"><span class="codigo">${linea.articulo.codigo}</span> - <br/><span class="descr">${linea.articulo.nombre}</span></td>
                                        <td> <fmt:formatNumber type="currency" value="${linea.articulo.pvp}"/></td>
                                        <td>
                                            <input class="cantidad" type="text" name="C_${linea.codigo}" size="3" value="${linea.cantidad}">
                                        </td>
                                        <td>
                                            <input type="text" name="F_${linea.codigo}" size="10" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${linea.fechaEntregaDeseada.time}"/>">			  
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <input class="submitb" type="submit" name="accion" value="Seguir comprando">
                            <input class="submitb" type="submit" name="accion" value="Guardar pedido">
                            <input class="submitb cerrar" type="submit" name="accion" value="Cerrar pedido">	
                            <input class="submitb cancelar" type="submit" name="accion" value="Cancelar">
                        </form>
                    </c:if>



                </div>
                <div class="clear"></div>
            </div>

            <div class="separa"></div>

            <%@include file="pie.html" %>

        </div>
    </body>
</html>




