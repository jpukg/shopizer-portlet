package com.salesmanager.web.admin.controller.customers;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerCriteria;
import com.salesmanager.core.business.customer.model.CustomerList;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class CustomerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	ZoneService zoneService;
	
	@Autowired
	LanguageService languageService;
	
	
	/**
	 * Customer details
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/customers/customer.html", method=RequestMethod.GET)
	public String displayCustomer(Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
			
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("customer", "customer");
		activeMenus.put("customer-create", "customer-create");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("customer");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		
		Customer customer = null;
		
		//if request.attribute contains id then get this customer from customerService
		if(id!=null && id!=0) {//edit mode
			
			//get from DB
			customer = customerService.getById(id);
			
		} else {
			 customer = new Customer();
		}
		//get list of countries (see merchant controller)
		Language language = (Language)request.getAttribute("LANGUAGE");				
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		//get list of zones
		List<Zone> zones = zoneService.list();
		
		model.addAttribute("zones", zones);
		model.addAttribute("countries", countries);
		model.addAttribute("customer", customer);
		return "admin-customer";	
		
	}
	
	@Secured("CUSTOMER")
	@RequestMapping(value="/admin/customers/save.html", method=RequestMethod.POST)
	public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception{
	
		String email_regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		Pattern pattern = Pattern.compile(email_regEx);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		//List<Language> languages = languageService.getLanguages();
		
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		if( StringUtils.isBlank(customer.getFirstname() ) ){
			 ObjectError error = new ObjectError("customerFirstName", messages.getMessage("NotEmpty.customer.FirstName", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(customer.getLastname() ) ){
			 ObjectError error = new ObjectError("customerLastName", messages.getMessage("NotEmpty.customer.LastName", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(customer.getStreetAddress() ) ){
			 ObjectError error = new ObjectError("customerStreetAddress", messages.getMessage("NotEmpty.customer.StreetAddress", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(customer.getCity() ) ){
			 ObjectError error = new ObjectError("customerCity", messages.getMessage("NotEmpty.customer.City", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(customer.getPostalCode() ) ){
			 ObjectError error = new ObjectError("customerPostCode", messages.getMessage("NotEmpty.customer.PostCode", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(customer.getTelephone() ) ){
			 ObjectError error = new ObjectError("customerTelephone", messages.getMessage("NotEmpty.customer.Telephone", locale));
			 result.addError(error);
		}
		
		if(!StringUtils.isBlank(customer.getEmailAddress() ) ){
			 java.util.regex.Matcher matcher = pattern.matcher(customer.getEmailAddress());
			 
			 if(!matcher.find()) {
				ObjectError error = new ObjectError("customerEmailAddress",messages.getMessage("Email.customer.EmailAddress", locale));
				result.addError(error);
			 }
		}else{
			ObjectError error = new ObjectError("customerEmailAddress",messages.getMessage("NotEmpty.customer.EmailAddress", locale));
			result.addError(error);
		}
		
		if( StringUtils.isBlank(customer.getBilling().getCompany() ) ){
			 ObjectError error = new ObjectError("billingName", messages.getMessage("NotEmpty.customer.billingCompany", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(customer.getBilling().getName() ) ){
			 ObjectError error = new ObjectError("billingName", messages.getMessage("NotEmpty.customer.billingName", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(customer.getBilling().getAddress() ) ){
			 ObjectError error = new ObjectError("billingAddress", messages.getMessage("NotEmpty.customer.billingStreetAddress", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(customer.getBilling().getCity() ) ){
			 ObjectError error = new ObjectError("billingCity",messages.getMessage("NotEmpty.customer.billingCity", locale));
			 result.addError(error);
		}
		 
		if( customer.getShowBillingStateList().equalsIgnoreCase("yes" ) && customer.getBilling().getZone().getCode() == null ){
			 ObjectError error = new ObjectError("billingState",messages.getMessage("NotEmpty.customer.billingState", locale));
			 result.addError(error);
			 
		}else if( customer.getShowBillingStateList().equalsIgnoreCase("no" ) && customer.getBilling().getState() == null ){
				 ObjectError error = new ObjectError("billingState",messages.getMessage("NotEmpty.customer.billingState", locale));
				 result.addError(error);
			
		}
		 
		if( StringUtils.isBlank(customer.getBilling().getPostalCode() ) ){
			 ObjectError error = new ObjectError("billingPostalCode", messages.getMessage("NotEmpty.customer.billingPostCode", locale));
			 result.addError(error);
		}
		
		//check if error from the @valid
		if (result.hasErrors()) {
			model.addAttribute("countries", countries);
			return "admin-customer";
		}
				
		Customer newCustomer = new Customer();

		if( customer.getId()!=null && customer.getId().longValue()>0 ) {
			newCustomer = customerService.getById( customer.getId() );
			
		}else{
			//  new customer set marchant_Id
			MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			newCustomer.setMerchantStore(merchantStore);
		}
		
		newCustomer.setFirstname( customer.getFirstname() );
		newCustomer.setLastname( customer.getLastname() );
		newCustomer.setEmailAddress(customer.getEmailAddress() );		
		newCustomer.setTelephone( customer.getTelephone() );
		newCustomer.setStreetAddress( customer.getStreetAddress() );
		newCustomer.setCity( customer.getCity() );
		newCustomer.setPostalCode( customer.getPostalCode() );
		
		
		//get Customer country/zone
		Country country = countryService.getByCode(customer.getCountry().getIsoCode());  		
		Country deliveryCountry = countryService.getByCode( customer.getDelivery().getCountry().getIsoCode()); 
		Country billingCountry  = countryService.getByCode( customer.getBilling().getCountry().getIsoCode()) ;

//		System.out.println( "************deliveryCountry.getIsoCode() = " + deliveryCountry.getIsoCode() );
		Zone zone = customer.getZone();
		Zone deliveryZone = customer.getDelivery().getZone();
		Zone billingZone  = customer.getBilling().getZone();
		
		if (customer.getShowCustomerStateList().equalsIgnoreCase("yes" )) {
			zone = zoneService.getByCode(zone.getCode());
			newCustomer.setState( null );			
		}else if (customer.getShowCustomerStateList().equalsIgnoreCase("no" )){
			zone = null ;
			newCustomer.setState( customer.getState() );			
		}
		
		if (customer.getShowDeliveryStateList().equalsIgnoreCase("yes" )) {
			deliveryZone = zoneService.getByCode(customer.getDelivery().getZone().getCode());
			customer.getDelivery().setState( null );
			
		}else if (customer.getShowDeliveryStateList().equalsIgnoreCase("no" )){
			deliveryZone = null ;
			customer.getDelivery().setState( customer.getDelivery().getState() );
		}
	
		if (customer.getShowBillingStateList().equalsIgnoreCase("yes" )) {
			billingZone = zoneService.getByCode(customer.getBilling().getZone().getCode());
			customer.getBilling().setState( null );
			
		}else if (customer.getShowBillingStateList().equalsIgnoreCase("no" )){
			billingZone = null ;
			customer.getBilling().setState( customer.getBilling().getState() );
		}
				
		newCustomer.setZone( zone);
		newCustomer.setCountry(country );
		
		customer.getDelivery().setZone(  deliveryZone);
		customer.getDelivery().setCountry(deliveryCountry );
		newCustomer.setDelivery( customer.getDelivery() );
		
		customer.getBilling().setZone(  billingZone);
		customer.getBilling().setCountry(billingCountry );
		newCustomer.setBilling( customer.getBilling()  );
		
		customerService.saveOrUpdate(newCustomer);
		
		model.addAttribute("customer", newCustomer);
		model.addAttribute("countries", countries);
		model.addAttribute("success","success");
		
		return "admin-customer";
		
	}

	
	/**
	 * List of customers
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/customers/list.html", method=RequestMethod.GET)
	public String displayCustomers(Model model,HttpServletRequest request) throws Exception {
		
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("customer", "customer");
		activeMenus.put("customer-list", "customer-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("customer");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
		return "admin-customers";
		
		
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/admin/customers/page.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody
	String pageCustomers(HttpServletRequest request,HttpServletResponse response) {


		AjaxPageableResponse resp = new AjaxPageableResponse();
		
		//Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		try {
			

			
			//Map<String,Country> countriesMap = countryService.getCountriesMap(language);
			
			
			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			String	email = request.getParameter("email");
			String name = request.getParameter("name");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String	country = request.getParameter("country");
			
			
			CustomerCriteria criteria = new CustomerCriteria();
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);
			
			if(!StringUtils.isBlank(email)) {
				criteria.setEmail(email);
			}
			
			if(!StringUtils.isBlank(name)) {
				criteria.setName(name);
			}
			
			if(!StringUtils.isBlank(country)) {
				criteria.setCountry(country);
			}
			
			if(!StringUtils.isBlank(firstName)) {
				criteria.setFirstName(firstName);
			}
			
			if(!StringUtils.isBlank(lastName)) {
				criteria.setLastName(lastName);
			}
			

			CustomerList customerList = customerService.listByStore(store,criteria);
			
			for(Customer customer : customerList.getCustomers()) {
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", customer.getId());
				entry.put("firstName", customer.getFirstname());
				entry.put("lastName", customer.getLastname());
				entry.put("email", customer.getEmailAddress());
				entry.put("country", customer.getCountry().getIsoCode());
				resp.addDataEntry(entry);
				
			}
			
		} catch (Exception e) {
			LOGGER.error("Error while paging orders", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
		
	
	}
	
		

}
