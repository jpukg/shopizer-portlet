package com.salesmanager.web.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.utils.ajax.AjaxResponse;


@Controller
public class ShoppingCartController {
	
	
	/**
	 * Retrieves a Shopping cart from the database
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/shoppingCart.html"}, method=RequestMethod.GET)
	public String displayShoppingCart(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//Looks in the HttpSession to see if a customer is logged in
		
		//shoppingCartService.getByCustomer(customerId);
		
		//set the cart in the HttpSession
		
		//calculate the price of each item by using ProductPriceUtils in sm-core
		//set each item price in ShoppingCartItem.price
		//return the ShoppingCartItem entity list in the model
		
		return ControllerConstants.Tiles.ShoppingCart.shoppingCart + ".bootstrap";
		
		
	}
	
	/**
	 * Add an item to the ShoppingCart (AJAX exposed method)
	 * @param id
	 * @param quantity
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/addShoppingCartItem.html"}, method=RequestMethod.GET)
	public @ResponseBody
	String addShoppingCartItem(@ModelAttribute Long id, @ModelAttribute Integer quantity, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		//Looks in the HttpSession to see if a customer is logged in
		//Looks in the HttpSession to see if a shopping cart exists
		
		
		//if a customer exists in http session
			//if a cart does not exist in httpsession
				//get cart from database
					//if a cart exist in the database add the item to the cart and put cart in httpsession and save to the database
					//else a cart does not exist in the database, create a new one, set the customer id, set the cart in the httpsession
			//else a cart exist in the httpsession, add item to httpsession cart and save to the database
		//else no customer in httpsession
			//if a cart does not exist in httpsession
				//create a new one, set the cart in the httpsession
			//else a cart exist in the httpsession, add item to httpsession cart and save to the database
		
		
		/**
		 * my concern is with the following : 
		 * 	what if you add item in the shopping cart as an anonymous user
		 *  later on you log in to process with checkout but the system retrieves a previous shopping cart saved in the database for that customer
		 *  in that case we need to synchronize both carts and the original one (the one with the customer id) supercedes the current cart in session
		 *  the sustem will have to deal with the original one and remove the latest
		 */
		
		
		//**more implementation details
		//calculate the price of each item by using ProductPriceUtils in sm-core
		//for each product in the shopping cart get the product
		//invoke productPriceUtils.getFinalOrderPrice
		//from FinalPrice get final price which is the calculated price given attributes and discounts
		//set each item price in ShoppingCartItem.price
		
		//add new item shoppingCartService.create
		
		//create JSON representation of the shopping cart
		
		//return the JSON structure in AjaxResponse
		
		//store the shopping cart in the http session
		
		AjaxResponse resp = new AjaxResponse();
		
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		return resp.toJSONString();
		
		
	}
	
	/**
	 * Removes an item from the Shopping Cart (AJAX exposed method)
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/removeShoppingCartItem.html"}, method=RequestMethod.GET)
	public @ResponseBody
	String removeShoppingCartItem(@ModelAttribute Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		//Looks in the HttpSession to see if a customer is logged in
		
		//get any shopping cart for this user
		
		//** need to check if the item has property, similar items may exist but with different properties
		String attributes = request.getParameter("attribute");//attributes id are sent as 1|2|5|
		//this will help with hte removal of the appropriate item
		
		//remove the item shoppingCartService.create
		
		//create JSON representation of the shopping cart
		
		//return the JSON structure in AjaxResponse
		
		//store the shopping cart in the http session
		
		AjaxResponse resp = new AjaxResponse();
		
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		return resp.toJSONString();
		
		
	}
	
	/**
	 * Update the quantity of an item in the Shopping Cart (AJAX exposed method)
	 * @param id
	 * @param quantity
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/updateShoppingCartItem.html"}, method=RequestMethod.GET)
	public @ResponseBody
	String updateShoppingCartItem(@ModelAttribute Long id, @ModelAttribute Integer quantity, HttpServletRequest request, HttpServletResponse response) throws Exception {
		


		
		AjaxResponse resp = new AjaxResponse();
		
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		return resp.toJSONString();
		
		
	}
		

}
