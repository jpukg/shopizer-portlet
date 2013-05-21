package com.salesmanager.web.shop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationshipType;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.web.constants.Constants;

@Controller
public class LandingController {
	
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ProductRelationshipService productRelationshipService;
	
	@Autowired
	private ProductPriceUtils productPriceUtils;
	
	@Autowired
	private CategoryService categoryService;
	
	//@Autowired
	//CategoryService categoryService;
	
	@RequestMapping(value={"/shop/home.html","/shop/","/shop"}, method=RequestMethod.GET)
	public String displayLanding(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		//store.getStorename();
		//store.getLanguages();
		//store.getDefaultLanguage();
		
		Content content = contentService.getByCode("LANDING_PAGE", store, language);

		//content.getDescriptions().get(0).getName();//title
		//content.getDescriptions().get(0).getMetatagDescription();
		//content.getDescriptions().get(0).getMetatagKeywords();
		//content.getDescriptions().get(0).getContent();
		
		//model.addAttribute("activeMenus",activeMenus);
		

		if(content!=null) {
			model.addAttribute("page",content.getDescriptions().get(0));
		}
		
		//root categories
		
		//featured items
		List<ProductRelationship> relationships = productRelationshipService.getByType(store, ProductRelationshipType.FEATURED_ITEM, language);
		List<com.salesmanager.web.entity.catalog.Product> featuredItems = new ArrayList<com.salesmanager.web.entity.catalog.Product>();
		for(ProductRelationship relationship : relationships) {
			
			Product product = relationship.getRelatedProduct();

			com.salesmanager.web.entity.catalog.Product proxyProduct = new com.salesmanager.web.entity.catalog.Product();
			proxyProduct.setProduct(product);
			proxyProduct.setDescription(product.getDescriptions().iterator().next());//get the first description from the set
			
			FinalPrice price = productPriceUtils.getFinalPrice(product);
			proxyProduct.setProductPrice(productPriceUtils.getFormatedAmountWithCurrency(store,price.getFinalPrice(),locale));

			featuredItems.add(proxyProduct);
		}
		
		//List<Category> categories = categoryService.listByDepth(store, 0);
		
		model.addAttribute("featuredItems", featuredItems);
		//model.addAttribute("categories", categories);
		
		StringBuilder template = new StringBuilder().append("landing.").append(store.getStoreTemplate());
		
		//return "landing.bootstrap";//template.toString()
		return template.toString();
	}

}
