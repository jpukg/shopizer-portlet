package com.salesmanager.web.admin.controller.products;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.catalog.product.model.review.ProductReviewDescription;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.review.ProductReviewService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;

@Controller
public class ProductReviewController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductReviewController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductReviewService productReviewService;
	
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/reviews.html", method=RequestMethod.GET)
	public String displayProductReviews(@RequestParam("id") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return "";

	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/reviews/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageProductReviews(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		AjaxResponse resp = new AjaxResponse();
		
		Long productId;
		Product product = null;
		
		try {
			productId = Long.parseLong(sProductId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return returnString;
		}

		
		try {

			product = productService.getById(productId);

			
			if(product==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return returnString;
			}
			
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return returnString;
			}
			
			
			Language language = (Language)request.getAttribute("LANGUAGE");

			
			List<ProductReview> reviews = productReviewService.getByProduct(product);
			


			for(ProductReview review : reviews) {
				Map entry = new HashMap();
				entry.put("reviewId", review.getId());
				entry.put("rating", review.getReviewRating().intValue());
				Set<ProductReviewDescription> descriptions = review.getDescriptions();
				String reviewDesc= "";
				for(ProductReviewDescription description : descriptions){
					if(description.getLanguage().getCode().equals(language.getCode())) {
						reviewDesc = description.getDescription();
					}
				}
				entry.put("description", reviewDesc);
				resp.addDataEntry(entry);
			}

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;


	}
	
	

}