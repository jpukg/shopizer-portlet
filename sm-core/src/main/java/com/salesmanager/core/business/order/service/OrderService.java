package com.salesmanager.core.business.order.service;

import java.util.List;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;

public interface OrderService extends SalesManagerEntityService<Long, Order> {
	
	Order getOrder(Long id);
	
	public OrderList listByStore(MerchantStore store, OrderCriteria criteria);
	
	public List<Order> listByStore(MerchantStore merchantStore);


	void saveOrUpdate(Order order) throws ServiceException;


	

}
