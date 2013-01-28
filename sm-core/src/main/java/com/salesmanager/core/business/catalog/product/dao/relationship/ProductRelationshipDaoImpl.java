package com.salesmanager.core.business.catalog.product.dao.relationship;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("productRelationshipDao")
public class ProductRelationshipDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductRelationship>
		implements ProductRelationshipDao {
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, String type, Product product, Language language) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		StringBuilder qs = new StringBuilder();
		qs.append("select pr from ProductRelationship as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("join fetch pr.relatedProduct rp ");
		qs.append("left join fetch rp.descriptions rpd ");

		qs.append("where pr.code=:code ");
		qs.append("and p.id=:id ");
		qs.append("and rpd.language.id=:langId");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("code", type);
    	q.setParameter("id", product.getId());
    	q.setParameter("langId", language.getId());


    	
    	@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, String type, Language language) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		StringBuilder qs = new StringBuilder();
		qs.append("select pr from ProductRelationship as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("join fetch pr.relatedProduct rp ");
		qs.append("left join fetch rp.descriptions rpd ");

		qs.append("where pr.code=:code ");
		qs.append("and rpd.language.id=:langId");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("code", type);
    	q.setParameter("langId", language.getId());


    	
    	@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}
	
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, String type) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		StringBuilder qs = new StringBuilder();
		qs.append("select pr from ProductRelationship as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("join fetch pr.relatedProduct rp ");
		qs.append("left join fetch rp.descriptions rpd ");

		qs.append("where pr.code=:code");




    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("code", type);


    	@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}
	
	@Override
	public List<ProductRelationship> listByProducts(Product product) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		StringBuilder qs = new StringBuilder();
		qs.append("select pr from ProductRelationship as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("left join fetch pr.relatedProduct rp ");
		qs.append("left join fetch p.descriptions pd ");
		qs.append("left join fetch rp.descriptions rpd ");

		qs.append("where p.id=:id");
		qs.append(" or rp.id=:id");




    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("id", product.getId());


    	@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, String type, Product product) {
		//QDSL cannot interpret the following query, that is why it is in native format
		
		StringBuilder qs = new StringBuilder();
		qs.append("select pr from ProductRelationship as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("where pr.code=:code ");
		qs.append("and p.id=:pId");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("code", type);
    	q.setParameter("pId", product.getId());


		@SuppressWarnings("unchecked")
		List<ProductRelationship> relations =  q.getResultList();

    	
    	return relations;
		

	}

	


}
