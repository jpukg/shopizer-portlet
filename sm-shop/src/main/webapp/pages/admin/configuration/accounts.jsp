<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				
<script>
	

	
</script>


<div class="tabbable">

  					
 					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">
  					
  					
  					<div class="tab-pane active" id="accounts-conf">


								<div class="sm-ui-component">
								<h3>//TODO title <s:message code="label.shipping.options" text="Shipping options" /></h3>	
								<br/>
								
							<div class="box">
							<span class="box-title"><p>Google analytics</p></span>
							<c:url var="saveAccountsConfiguration" value="/admin/CHANGE-URL.html"/>
							<form:form method="POST" commandName="configuration" action="${saveAccountsConfiguration}">

      							
      								<form:errors path="*" cssClass="alert alert-error" element="div" />
									<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    

                  					
                  					<div class="control-group">
                        				<label>//GA tracking<s:message code="label.shipping.handlingfees" text="Handling fees"/></label>
                        				<div class="controls">
											<form:input cssClass="input-large" path="handlingFeesText" />
                        				</div>
	                                	<span class="help-inline"><form:errors path="handlingFeesText" cssClass="error" /></span>
	                        		</div>

	                        		<div class="form-actions">
                  						<div class="pull-right">
                  							<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  						</div>
            	 					</div>
					                  

            	 			</form:form>
							
							


      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>