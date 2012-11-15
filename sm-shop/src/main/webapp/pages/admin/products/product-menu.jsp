<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>

    <ul class="nav nav-pills">
    
    
    					
    
    	<li class="enabled"><a href="<c:url value="/admin/products/editProduct.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.details" text="Product details" /></a></li>
    	<li class="enabled"><a href="<c:url value="/admin/products/prices.html" />"><s:message code="label.product.prices" text="Product prices" /></a></li>
    	<li class="enabled"><a href="#"><s:message code="label.product.attributes" text="Attributes" /></a></li>
    
    </ul>