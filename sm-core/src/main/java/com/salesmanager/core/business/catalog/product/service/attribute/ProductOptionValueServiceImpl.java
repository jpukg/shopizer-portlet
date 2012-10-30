package com.salesmanager.core.business.catalog.product.service.attribute;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.attribute.ProductOptionValueDao;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Service("productOptionValueService")
public class ProductOptionValueServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductOptionValue> implements
		ProductOptionValueService {

	@Autowired
	private ProductAttributeService productAttributeService;
	
	private ProductOptionValueDao productOptionValueDao;
	
	@Autowired
	public ProductOptionValueServiceImpl(
			ProductOptionValueDao productOptionValueDao) {
			super(productOptionValueDao);
			this.productOptionValueDao = productOptionValueDao;
	}
	
	
	@Override
	public List<ProductOptionValue> listByStore(MerchantStore store, Language language) throws ServiceException {
		
		
		return productOptionValueDao.listByStore(store, language);
		
		
	}
	
	@Override
	public ProductOptionValue getById(MerchantStore store, Long id) throws ServiceException {
		
		try {
			return productOptionValueDao.getById(store, id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	@Override
	public List<ProductOptionValue> getByName(MerchantStore store, String name, Language language) throws ServiceException {
		
		try {
			return productOptionValueDao.getByName(store, name, language);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}
	
	@Override
	public void saveOrUpdate(ProductOptionValue entity) throws ServiceException {
		
		
		//save or update (persist and attach entities
		if(entity.getId()!=null && entity.getId()>0) {

			super.update(entity);
			
		} else {
			
			super.save(entity);
			
		}
		
	}
	
	
	public void delete(ProductOptionValue entity) throws ServiceException {
		
		//remove all attributes having this option
		List<ProductAttribute> attributes = productAttributeService.getByOptionValueId(entity.getMerchantSore(), entity);
		
		for(ProductAttribute attribute : attributes) {
			productAttributeService.delete(attribute);
		}
		
		ProductOptionValue option = this.getById(entity.getMerchantSore(), entity.getId());
		
		//remove option
		super.delete(option);
		
	}



}
