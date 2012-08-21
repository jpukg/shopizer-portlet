package com.salesmanager.web.admin.controller.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionDescription;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;

@Controller
public class OptionsController {
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	ProductOptionService productOptionService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OptionsController.class);
	
	
	
	@RequestMapping(value="/admin/options/options.html", method=RequestMethod.GET)
	public String displayOptions(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);

		List<Language> languages = languageService.list();//TODO get supported languages from MerchantStore
		//List<Language> languages = store.getLanguages();
		
		List<ProductOptionDescription> descriptions = new ArrayList<ProductOptionDescription>();
		
		ProductOption option = new ProductOption();
		
		for(Language l : languages) {
			
			ProductOptionDescription desc = new ProductOptionDescription();
			desc.setLanguage(l);
			descriptions.add(desc);
			
		}

		option.setDescriptions(descriptions);
	


	
		model.addAttribute("option", option);

		
		return "catalogue-options";
		
		
		
	}

	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/admin/options/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageOptions(HttpServletRequest request, HttpServletResponse response) {
		
		String optionName = request.getParameter("name");


		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			
			Language language = (Language)request.getAttribute("LANGUAGE");	
		
			MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
			
			List<ProductOption> options = null;
					
			if(!StringUtils.isBlank(optionName)) {
				
				//todo get options
				//categoryService.getByName(store, categoryName);
				
			} else {
				
				options = productOptionService.listByStore(store, language);
				
			}
					
					
			
			for(ProductOption option : options) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("optionId", option.getId());
				
				ProductOptionDescription description = option.getDescriptions().get(0);
				
				entry.put("name", description.getName());
				entry.put("type", option.getProductOptionType());//TODO resolve with option type label
				resp.addDataEntry(entry);
				
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			

		
		} catch (Exception e) {
			LOGGER.error("Error while paging options", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
		
		
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-options", "catalogue-options");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}