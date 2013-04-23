<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>

<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>

<script>

$(document).ready(function() {
	
		if($("#code").val()=="") {
			$('.btn').addClass('disabled');
		}

		<c:if test="${customer.state!=null && customer.state!=''}">
			$('.zone-list').hide();          
			$('#stateOther').show(); 
			$("input[name='showCustomerStateList']").val('no');
			$('#stateOther').val('<c:out value="${customer.state}"/>');
		</c:if>
		<c:if test="${customer.state==null || customer.state==''}">
			$('.zone-list').show();           
			$('#stateOther').hide();
			$("input[name='showCustomerStateList']").val('yes');
			getZones('<c:out value="${customer.country.isoCode}" />'); 
		</c:if>
		
		<c:if test="${customer.delivery.state!=null && customer.delivery.state!=''}">  
			$('.delivery-zone-list').hide();  
			$('#delstateOther').show(); 
			$("input[name='showDeliveryStateList']").val('no');
			$('#delstateOther').val('<c:out value="${customer.delivery.state}"/>');
		</c:if>
		<c:if test="${customer.delivery.state==null || customer.delivery.state==''}"> 
			$('.delivery-zone-list').show();			
			$('#delstateOther').hide();
			$("input[name='showDeliveryStateList']").val('yes');
			getDeliveryZones('<c:out value="${customer.delivery.country.isoCode}" />'); 
		</c:if>
	
		<c:if test="${customer.billing.state!=null && customer.billing.state!=''}">
			$('.billing-zone-list').hide();          
			$('#bilstateOther').show(); 
			$("input[name='showBillingStateList']").val('no');
			$('#bilstateOther').val('<c:out value="${customer.billing.state}"/>');
		</c:if>
	
		<c:if test="${customer.billing.state==null || customer.billing.state==''}">  
			$('.billing.zone-list').show();           
			$('#bilstateOther').hide();
			$("input[name='showBillingStateList']").val('yes');
			getBillingZones('<c:out value="${customer.billing.country.isoCode}" />'); 
		</c:if>
	
		
	
		$(".country-list").change(function() {
			getZones($(this).val());
	    })
	
	    $(".billing-country-list").change(function() {
			getBillingZones($(this).val());
	    })
	
	    $(".delivery-country-list").change(function() {
	    	getDeliveryZones($(this).val());
	    })
});

$.fn.addItems = function(data) {
    $(".zone-list > option").remove();
        return this.each(function() {
            var list = this;
            $.each(data, function(index, itemData) {
                var option = new Option(itemData.name, itemData.code);
                list.add(option);
            });
     });
};

function getZones(countryCode){
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/admin/reference/provinces.html"/>',
	  data: 'countryCode=' + countryCode,
	  dataType: 'json',
	  success: function(response){

			var status = isc.XMLTools.selectObjects(response, "/response/status");
			if(status==0 || status ==9999) {
				
				var data = isc.XMLTools.selectObjects(response, "/response/data");
				if(data && data.length>0) {
					$("input[name='showCustomerStateList']").val('yes');
					$('.zone-list').show();  
					$('#stateOther').hide();
					$(".zone-list").addItems(data);					
					<c:if test="${customer.zone!=null}">
						$('.zone-list').val('<c:out value="${customer.zone.code}"/>');
					</c:if>
				} else {
					$("input[name='showCustomerStateList']").val('no');
					$('.zone-list').hide();             
					$('#stateOther').show();
					<c:if test="${stateOther!=null}">
						$('#stateOther').val('<c:out value="${customer.state}"/>');
					</c:if>
				}
			} else {
				$('.zone-list').hide();             
				$('#stateOther').show();
			}

	  
	  },
	  error: function(xhr, textStatus, errorThrown) {
	  	alert('error ' + errorThrown);
	  }
	  
	});
}															

$.fn.addDeliveryItems = function(data) {
    $(".delivery-zone-list > option").remove();
        return this.each(function() {
            var list = this;
            $.each(data, function(index, itemData) {
                var option = new Option(itemData.name, itemData.code);
                list.add(option);
            });
     });
};

function getDeliveryZones(countryCode){
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/admin/reference/provinces.html"/>',
	  data: 'countryCode=' + countryCode,
	  dataType: 'json',
	  success: function(response){

			var status = isc.XMLTools.selectObjects(response, "/response/status");  
			if(status==0 || status ==9999) {
				
				var data = isc.XMLTools.selectObjects(response, "/response/data");
				if(data && data.length>0) {
					$("input[name='showDeliveryStateList']").val('yes');
					$('.delivery-zone-list').show();  
					$('#delstateOther').hide();
					$(".delivery-zone-list").addDeliveryItems(data);					
					<c:if test="${customer.delivery.zone!=null}">
						$('.delivery-zone-list').val('<c:out value="${customer.delivery.zone.code}"/>');
					</c:if>
				} else {
					$("input[name='showDeliveryStateList']").val('no');
					$('.delivery-zone-list').hide();             
					$('#delstateOther').show();
					<c:if test="${delstateOther!=null}">
						$('#delstateOther').val('<c:out value="${customer.delivery.state}"/>');
					</c:if>
				}
			} else {
				$('.delivery-zone-list').hide();             
				$('#delstateOther').show();
			}

	  
	  },
	  error: function(xhr, textStatus, errorThrown) {
	  	alert('error ' + errorThrown);
	  }
	});
}

$.fn.addBillingItems = function(data) {
	    $(".billing-zone-list > option").remove();
	        return this.each(function() {
	            var list = this;
	            $.each(data, function(index, itemData) {
	                var option = new Option(itemData.name, itemData.code);
	                list.add(option);
	            });
	     });
};

function getBillingZones(countryCode){
		$.ajax({
		  type: 'POST',
		  url: '<c:url value="/admin/reference/provinces.html"/>',
		  data: 'countryCode=' + countryCode,
		  dataType: 'json',
		  success: function(response){

				var status = isc.XMLTools.selectObjects(response, "/response/status");
				if(status==0 || status ==9999) {
					
					var data = isc.XMLTools.selectObjects(response, "/response/data");
					if(data && data.length>0) {
						$("input[name='showBillingStateList']").val('yes');
						$('.billing-zone-list').show();  
						$('#bilstateOther').hide();
						$(".billing-zone-list").addBillingItems(data);					
						<c:if test="${customer.billing.zone!=null}">
							$('.billing-zone-list').val('<c:out value="${customer.billing.zone.code}"/>');
						</c:if>
					} else {
						$("input[name='showBillingStateList']").val('no');
						$('.billing-zone-list').hide();             
						$('#bilstateOther').show();
						<c:if test="${bilstateOther!=null}">
							$('#bilstateOther').val('<c:out value="${customer.billing.state}"/>');
						</c:if>
					}
				} else {
					$('.billing-zone-list').hide();             
					$('#bilstateOther').show();
				}

		  
		  },
		  error: function(xhr, textStatus, errorThrown) {
		  	alert('error ' + errorThrown);
		  }
		  
		});
}
</script>


<div class="tabbable">


				<jsp:include page="/common/adminTabs.jsp" />
				
				<h3>
					<c:choose>
						<c:when test="${customer.id!=null && customer.id>0}">
								<s:message code="label.customer.editcustomer" text="Edit Customer" /> <c:out value="${category.code}"/>
						</c:when>
						<c:otherwise>
								<s:message code="label.customer.createcustomer" text="Create Customer" />
						</c:otherwise>
					</c:choose>
					
				</h3>	
				<br/>
				


				<c:url var="customer" value="/admin/customers/save.html"/>


				<form:form method="POST" commandName="customer" action="${customer}">
				
					<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="customer.success" class="alert alert-success" 
							style="<c:choose>
								<c:when test="${success!=null}">display:block;</c:when>
								<c:otherwise>display:none;</c:otherwise></c:choose>">
								<s:message code="message.success" text="Request successful"/>
					</div>    
					
					<form:hidden path="id" /> 
					<form:hidden path="merchantStore.id" />	
					<form:hidden path="showCustomerStateList" />
					<form:hidden path="showBillingStateList" /> 
					<form:hidden path="showDeliveryStateList" />  	
						
				<div class="span4">  
					<h6>Customer address</h6>
					
	      			<div class="control-group">
	                        <label><s:message code="label.customer.firstname" text="First Name"/></label>
	                        <div class="controls">
	                        		<form:input cssClass="input-large highlight"  maxlength="32" path="firstname" />
	                                <span class="help-inline"><form:errors path="firstname" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.lastname" text="Last Name"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large highlight"  maxlength="32" path="lastname" />
	                                    <span class="help-inline"><form:errors path="lastname" cssClass="error" /></span>
	                        </div>
	
	                  </div>
	                  
	                 <div class="control-group">
	                        <label><s:message code="label.customer.email" text="Email"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large highlight"  maxlength="96" path="emailAddress" />
	                                    <span class="help-inline"><form:errors path="emailAddress" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                   <div class="control-group">
	                        <label><s:message code="label.customer.telephone" text="Phone"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large highlight"  maxlength="32" path="telephone" />
	                                    <span class="help-inline"><form:errors path="telephone" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.streetaddress" text="StreetAddress"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large highlight"  maxlength="256" path="streetAddress" />
	                                    <span class="help-inline"><form:errors path="streetAddress" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.city" text="City"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large highlight" maxlength="100"  path="city" />
	                                    <span class="help-inline"><form:errors path="city" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.country" text="Country"/></label>
	                        <div class="controls"> 
				       							
	       							<form:select cssClass="country-list highlight" path="country.isoCode">
		  								<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
	       							</form:select>
                                 	<span class="help-inline"><form:errors path="country.isoCode" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group"> 
	                        <label><s:message code="label.customer.zone" text="State / Province"/></label>
	                        <div class="controls">		       							
	       							<form:select cssClass="zone-list highlight" path="zone.code"/>
                      				<form:input  class="input-large highlight" id="stateOther"  maxlength="100" name="stateOther" path="state" /> 				       							
                                 	<span class="help-inline"><form:errors path="zone.code" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.postalcode" text="Postalcode"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large highlight" maxlength="20" path="postalCode" />
	                                    <span class="help-inline"><form:errors path="postalCode" cssClass="error" /></span>
	                        </div>
	                  </div>
	               
				</div>  
	
			     <div class="offset5">	
	 					
				<h6>Shipping address</h6>
				<address>
			            <div class="controls">
		              		<label><s:message code="label.customer.shipping.company" text="Company"/></label>
		              		<form:input  cssClass="input-large"  maxlength="100" path="delivery.company"/>	
			            </div>
			            <div class="controls">
		              		<label><s:message code="label.customer.shipping.name" text="Name"/></label>
		              		<form:input  cssClass="input-large"  maxlength="64" path="delivery.name"/>	
			            </div>
			            <div class="controls">
			            	<label><s:message code="label.customer.shipping.streetaddress" text="Street Address"/></label>
				 			<form:input  cssClass="input-large"  maxlength="256" path="delivery.address"/>		 				
			            </div>
			            <div class="controls">
			            	<label><s:message code="label.customer.shipping.city" text="City"/></label>
				 			<form:input  cssClass="input-large"  maxlength="100" path="delivery.city"/>
			            </div>
	            
 	 		           <div class="control-group">
	                        <label><s:message code="label.customer.shipping.country" text="Country"/></label>
	                        <div class="controls"> 				       							
	       							<form:select cssClass="delivery-country-list highlight" path="delivery.country.isoCode">
		  								<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
	       							</form:select>
                                 	<span class="help-inline"><form:errors path="delivery.country.isoCode" cssClass="error" /></span>
	                        </div>
	                    </div>  
     	  	         
	                    <div class="control-group"> 
	                        <label><s:message code="label.customer.shipping.zone" text="State / Province"/></label>
	                        <div class="controls">		       							
	       							<form:select cssClass="delivery-zone-list" path="delivery.zone.code"/>
                      				<form:input  class="input-large" id="delstateOther"  maxlength="100" name="delstateOther" path="delivery.state" /> 				       							
                                 	<span class="help-inline"><form:errors path="delivery.zone.code" cssClass="error" /></span>
	                        </div> 
	                    </div>  
	                    
	                    <div class="controls">
	                   		<label><s:message code="label.customer.shipping.postalcode" text="Postal code"/></label>
			 				<form:input id="deliveryPostalCode" cssClass="input-large" maxlength="20"  path="delivery.postalCode"/>
			 				<span class="help-inline"><form:errors path="delivery.postalCode" cssClass="error" /></span>
			            </div>	       	            	            	            				
				</address>	
			 
				<br/>

			    <h6>Billing address</h6>
				<address>
						<div class="controls">
		              		<label><s:message code="label.customer.billing.company" text="Company"/></label>
		              		<form:input  cssClass="input-large highlight"  maxlength="100" path="billing.company"/>	
			            </div>
			            <div class="controls">
		              		<label><s:message code="label.customer.billing.name" text="Name"/></label>
			 				<form:input  cssClass="input-large highlight"  maxlength="64"  path="billing.name"/>				 							
			            </div>
			            <div class="controls">
			            	<label><s:message code="label.customer.billing.streetaddress" text="Street Address"/></label>
				 			<form:input  cssClass="input-large highlight"  maxlength="256"  path="billing.address"/>		 				
			            </div>
			            <div class="controls">
			            	<label><s:message code="label.customer.billing.city" text="City"/></label>
				 			<form:input  cssClass="input-large highlight"  maxlength="100" path="billing.city"/>
			            </div>
		            
 	 		            <div class="control-group">
	                        <label><s:message code="label.customer.billing.country" text="Country"/></label>
	                        <div class="controls"> 				       							
	       							<form:select cssClass="billing-country-list highlight" path="billing.country.isoCode">
		  								<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
	       							</form:select>
                                 	<span class="help-inline"><form:errors path="billing.country.isoCode" cssClass="error" /></span>
	                        </div>  
	                    </div> 
	                 
	                    <div class="control-group"> 
	                        <label><s:message code="label.customer.billing.zone" text="State / Province"/></label>
	                        <div class="controls">		       							
	       							<form:select cssClass="billing-zone-list highlight" path="billing.zone.code"/>
                      				<form:input  class="input-large highlight" id="bilstateOther" maxlength="100"  name="bilstateOther" path="billing.state" /> 				       							
                                 	<span class="help-inline"><form:errors path="billing.zone.code" cssClass="error" /></span>
	                        </div>
	                    </div>  
	                  
	                    <div class="controls">
	                   		<label><s:message code="label.customer.billing.postalcode" text="Postal code"/></label>
			 				<form:input id="billingPostalCode" cssClass="input-large highlight" maxlength="20"  path="billing.postalCode"/>
			 				<span class="help-inline"><form:errors path="billing.postalCode" cssClass="error" /></span>
			            </div>	     
		              	            	            	            				
				</address>			
		  	 </div> 
		        <div class="form-actions">
                 	  <div class="pull-right">
                 			<button type="submit" class="btn btn-success"><s:message code="button.label.save" text="Save"/></button>
                 	  </div> 
           	   </div>


      					
				</form:form>

</div>