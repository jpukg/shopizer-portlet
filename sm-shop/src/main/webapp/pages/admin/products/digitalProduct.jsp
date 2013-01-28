<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>   

<%@ page session="false"%>



<script type="text/javascript">
	
	function removeFile(fileId){
			$("#store.error").show();
			$.ajax({
			  type: 'POST',
			  url: '<c:url value="/admin/products/product/removeProduct.html"/>?fileId=' + fileId,
			  dataType: 'json',
			  success: function(response){
		
					var status = isc.XMLTools.selectObjects(response, "/response/status");
					if(status==0 || status ==9999) {
						
						//remove delete
						$("#productControlRemove").html('');
						//add field
						$("#productControl").html('<input class=\"input-file\" id=\"productImage\" name=\"productImage\" type=\"file\">');
						$(".alert-success").show();
						
					} else {
						
						//display message
						$(".alert-error").show();
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

	<div class="tab-content">

		<div class="tab-pane active" id="catalogue-section">
		
				<c:url var="saveProductFile" value="/admin/products/product/saveDigitalProduct.html" />
				<form:form method="POST" enctype="multipart/form-data" commandName="productFiles" action="${saveProductFile}">

					<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success"
						style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
						<s:message code="message.success" text="Request successfull" />
					</div>
					<form:hidden path="product.id" />
				
					<!-- hidden when creating the product -->
					<div class="control-group">
						<label><s:message code="label.storelogo" text="Store logo"/>&nbsp;<c:if test="${file==null}"><span id="productControlRemove"> - <a href="#" onClick="removeFile('${file.id}')"><s:message code="label.generic.remove" text="Remove"/></a></span></c:if></label>
						<div class="controls" id="fileControl">
						
									   <c:choose>
				                        		<c:when test="${file==null}">
				                                    <input class="input-file" id="file" name="file[0]" type="file"><br/>
				                                </c:when>
				                                <c:otherwise>
				                                	
				                                </c:otherwise>
			                            </c:choose>
		
						</div>
					</div>
					<div class="form-actions">
						<div class="pull-right">
							<button type="submit" class="btn btn-success">
								<s:message code="button.label.submit2" text="Submit" />
							</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>	
				

				
				
				
				


