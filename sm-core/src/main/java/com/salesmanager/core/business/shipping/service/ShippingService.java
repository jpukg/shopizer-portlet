package com.salesmanager.core.business.shipping.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.shipping.model.PackageDetails;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.shipping.model.ShippingProduct;
import com.salesmanager.core.business.shipping.model.ShippingQuote;
import com.salesmanager.core.business.system.model.CustomIntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;

public interface ShippingService {

	/**
	 * Returns a list of supported countries configured by merchant
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	public  List<String> getSupportedCountries(MerchantStore store)
			throws ServiceException;

	public  void setSupportedCountries(MerchantStore store,
			List<String> countryCodes) throws ServiceException;

	/**
	 * Returns a list of available shipping modules
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	public List<IntegrationModule> getShippingMethods(MerchantStore store)
			throws ServiceException;

	
	/**
	 * Returns a list of configured shipping modules for a given merchant
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	Map<String, IntegrationConfiguration> getShippingModulesConfigured(
			MerchantStore store) throws ServiceException;

	/**
	 * Adds a Shipping configuration
	 * @param configuration
	 * @param store
	 * @throws ServiceException
	 */
	void saveShippingQuoteModuleConfiguration(IntegrationConfiguration configuration,
			MerchantStore store) throws ServiceException;

	/**
	 * ShippingType (NATIONAL, INTERNATIONSL)
	 * ShippingBasisType (SHIPPING, BILLING)
	 * ShippingPriceOptionType (ALL, LEAST, HIGHEST)
	 * Packages
	 * Handling
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	ShippingConfiguration getShippingConfiguration(MerchantStore store)
			throws ServiceException;

	/**
	 * Saves ShippingConfiguration for a given MerchantStore
	 * @param shippingConfiguration
	 * @param store
	 * @throws ServiceException
	 */
	void saveShippingConfiguration(ShippingConfiguration shippingConfiguration,
			MerchantStore store) throws ServiceException;

	void removeShippingQuoteModuleConfiguration(String moduleCode,
			MerchantStore store) throws ServiceException;

	/**
	 * Provides detailed information on boxes that will be used
	 * when getting a ShippingQuote
	 * @param products
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	List<PackageDetails> getPackagesDetails(List<ShippingProduct> products,
			MerchantStore store) throws ServiceException;

	/**
	 * Get a list of ShippingQuote from a configured
	 * shipping gateway
	 * @param store
	 * @param customer
	 * @param products
	 * @param locale
	 * @return
	 * @throws ServiceException
	 */
	ShippingQuote getShippingQuote(MerchantStore store, Customer customer,
			List<ShippingProduct> products, Locale locale)
			throws ServiceException;
	

	/**
	 * Returns a shipping module configuration given a moduleCode
	 * @param moduleCode
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	IntegrationConfiguration getShippingConfiguration(String moduleCode,
			MerchantStore store) throws ServiceException;
	
	/**
	 * Retrieves the custom configuration for a given module
	 * @param moduleCode
	 * @param store
	 * @return
	 * @throws ServiceException
	 */


	CustomIntegrationConfiguration getCustomShippingConfiguration(
			String moduleCode, MerchantStore store) throws ServiceException;

	/**
	 * Weight based configuration
	 * @param moduleCode
	 * @param shippingConfiguration
	 * @param store
	 * @throws ServiceException
	 */
	void saveCustomShippingConfiguration(String moduleCode,
			CustomIntegrationConfiguration shippingConfiguration,
			MerchantStore store) throws ServiceException;

	/**
	 * Removes a custom shipping quote
	 * module
	 * @param moduleCode
	 * @param store
	 * @throws ServiceException
	 */
	void removeCustomShippingQuoteModuleConfiguration(String moduleCode,
			MerchantStore store) throws ServiceException;




}