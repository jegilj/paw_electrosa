

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" lang="es-ES">
        <title>Electrosa >> cat&aacute;logo</title>
        <meta name="description" content="Aplicación de prueba de Programación de aplicaciones Web; Grado en informática; Universidad de La Rioja." lang="es-ES">
        <meta name="keywords" content="electrodomesticos" lang="es-ES">
        <meta name="language" content="es-ES">
        <meta name="robots" content="index,follow">

        <link href="css/electrosa.css" rel="stylesheet" media="all" type="text/css">
        <link href="css/catalogoMosaico.css" rel="stylesheet" media="all" type="text/css">
        <!--    <link href="css/catalogoLista.css" rel="stylesheet" media="all" type="text/css"> -->
    </head>

    <body >
        <%@include  file="cabecera.html" %>

        <div class="sombra">
            <div class="nucleo">

                <div id="migas">
                    <a href="index.html" title="Inicio" >Inicio</a>&nbsp; | &nbsp; 
                    <a href="generica.html" title="Hojear catalogo">Hojear catalogo</a>	<!-- &nbsp; | &nbsp; 
                    <a href="..." title="Otra cosa">Otra cosa</a>   -->	
                </div>
                <div class="contenido">
                    <h1>Nuestros productos</h1>
                    <p>Puede buscar los productos que necesite en nuestro cat&aacute;logo. Lo hemos organizado por marcas, tipo de electrodom&eacute;stico y rango de precios. Lo precios indicados en rojo corresponden a ofertas. </p>
                    <div class="filtroCatalogo">
                        <form name="filtroCatalogo" id="filtroCatalogo" action="BuscarArticulos">

                            <label for="tipo">Tipo: </label>
                            <select name="tipo" id="tipo">
                                <option value="-1">- Cualquiera -</option>
                                <c:forEach var="t" items="${tiposArticulos}">
                                    <option value="${t}" ${(t eq tipo ? ' selected' : '')} >${t}</option>
                                </c:forEach>
                            </select>

                            <label for="fabricante">Fabricante :</label>
                            <select name="fabricante" id="fabricante">
                                <option value="-1">- Cualquiera -</option>
                                <c:forEach var="f" items="${fabricantes}">
                                    <option value="${f}" ${(f eq fabricante ? ' selected':'')}>${f}</option>
                                </c:forEach>
                            </select>

                            <label for="precio">Precio: </label>
                            <select name="precio" id="precio">
                                <option value="-1">- Cualquiera -</option>
                                <option value="10-50" ${(precio=="10-50" ? 'selected': '')}>10-50 &euro;</option>
                                <option value="50-100" ${(precio=="50-100" ? 'selected': '')}>50-100 &euro;</option>
                                <option value="100-200" ${(precio=="100-200" ? 'selected': '')}>100-200 &euro;</option>
                                <option value="200-500" ${(precio=="200-500" ? 'selected': '')}>200-500 &euro;</option>
                                <option value="500-1000" ${(precio=="500-1000" ? 'selected': '')}>500-1000 &euro;</option>
                                <option value="1000" ${(precio=="1000" ? 'selected': '')}>Mas de 1000 &euro;</option>
                            </select>



                            <input name="buscar" id="buscar" type="image" title="Buscar" src="img/search25.png" alt="Buscar">
                            <p/>
                            <p/>
                            <div>
                                <label for="nombre">Nombre :</label><input type="text" name="nombre" id="nombre" value="${nombre}" />
                                <label for="codigo">Código :</label><input type="text" name="codigo" id="codigo" value="${codigo}" />
                            </div>


                        </form>

                        <div class="modovisual">
                            <a href="catalogo.html">Mosaico</a> &nbsp; | &nbsp; <a href="catalogo.html">Lista</a>
                        </div>
                        <div class="clear"></div>
                    </div>


                    <c:if test="${numRegs != null}">
                        <div class="resumResul redondeo">
                            Encontrados ${numRegs} artículos.
                            <c:if test="${numRegs > 0}">
                                Mostrando página ${paginaActual} de ${numPags}.
                            </c:if>
                            <c:if test="${numRegs==1}">
                                <c:forEach var="art" items="${articulos}">
                                    <c:url var="ficha" value="FichaArticulo"><c:param name="cart" value="${art.codigo}"/></c:url>
                                    <c:redirect url='${ficha}'/>
                                </c:forEach>

                            </c:if>
                            <span class="paginador">
                                <c:if test="${paginaActual != 1}">
                                    <a href="${direccion}${paginador.anterior(paginaActual)}">Anterior</a> 
                                </c:if>

                                <c:forEach var="pags" items="${paginador.adyacentes(paginaActual)}">
                                    <c:if test="${paginaActual == pags}">
                                        <span>${pags}</span>
                                    </c:if>
                                    <c:if test="${paginaActual != pags}">
                                        <a href="${direccion}${pags}">${pags}</a>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${paginaActual != paginador.numPaginas}">
                                    <a href="${direccion}${paginador.siguiente(paginaActual)}">Siguiente</a>
                                </c:if>

                            </span>
                        </div>
                    </c:if>


                    <ul class="resultBusqueda">
                        <c:forEach var="art" items="${articulos}">
                            <li class="item">
                                <div class="foto">
                                    <c:url var="ficha" value="FichaArticulo"><c:param name="cart" value="${art.codigo}"/></c:url>
                                    <a href='${ficha}'><img src="img/fotosElectr/${art.foto}" alt="${art.codigo}" longdesc="${art.descripcion}" width="80"></a>
                                </div>
                                <div class="datos">
                                    <span>${art.nombre}</span>
                                    <div class="precio">
                                        <span>${art.pvp} &euro;</span>
                                    </div>
                                    <div class="carro">
                                        <a href="clientes/GestionaPedido?accion=Comprar&ca=${art.codigo}"><img src="img/shopcartadd_16x16.png" title="Añadir a mi carro de la compra"></a>
                                    </div>
                                </div>			  
                                <div class="codigo"><a href='${ficha}'>${art.codigo}</a></div>
                            </li>
                        </c:forEach>

                    </ul>			
                    <div class="clear"></div>

                </div>
            </div>

            <%@include  file="pie.html" %>

        </div>
    </body>
</html>