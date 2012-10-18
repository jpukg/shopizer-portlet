package com.salesmanager.core.business.catalog.product.model.price;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.utils.CloneUtils;

@Entity
@Table(name = "PRODUCT_PRICE", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class ProductPrice extends SalesManagerEntity<Long, ProductPrice> {
	private static final long serialVersionUID = -9186473817468772165L;

	@Id
	@Column(name = "PRODUCT_PRICE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_PRICE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productPrice", cascade = CascadeType.ALL)
	private Set<ProductPriceDescription> descriptions = new HashSet<ProductPriceDescription>();

	@Column(name = "PRODUCT_PRICE_TYP_ID")
	private Integer productPriceTypeId;

	@NotNull
	@Column(name = "PRODUCT_PRICE_AMOUNT")
	private BigDecimal productPriceAmount = new BigDecimal(0);;

	@Column(name = "PRODUCT_PRICE_MOD_NM", length=20)
	private String productPriceModuleName;

	@Column(name = "DEFAULT_PRICE")
	private Boolean defaultPrice = false;

	@Temporal(TemporalType.DATE)
	@Column(name = "PRODUCT_PRICE_SPECIAL_ST_DATE")
	private Date productPriceSpecialStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "PRODUCT_PRICE_SPECIAL_END_DATE")
	private Date productPriceSpecialEndDate;

	@Column(name = "PRODUCT_PRICE_SPECIAL_DAYS")
	private Integer productPriceSpecialDurationDays;

	@Column(name = "PRODUCT_PRICE_SPECIAL_AMOUNT")
	private BigDecimal productPriceSpecialAmount;
	

	@ManyToOne(targetEntity = ProductAvailability.class)
	@JoinColumn(name = "PRODUCT_AVAIL_ID", nullable = false)
	private ProductAvailability productPriceAvailability;

	public ProductPrice() {
	}
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}


	public Integer getProductPriceTypeId() {
		return productPriceTypeId;
	}

	public void setProductPriceTypeId(Integer productPriceTypeId) {
		this.productPriceTypeId = productPriceTypeId;
	}

	public BigDecimal getProductPriceAmount() {
		return productPriceAmount;
	}

	public void setProductPriceAmount(BigDecimal productPriceAmount) {
		this.productPriceAmount = productPriceAmount;
	}

	public String getProductPriceModuleName() {
		return productPriceModuleName;
	}

	public void setProductPriceModuleName(String productPriceModuleName) {
		this.productPriceModuleName = productPriceModuleName;
	}

	public Boolean isDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(Boolean defaultPrice) {
		this.defaultPrice = defaultPrice;
	}
	
	public Date getProductPriceSpecialStartDate() {
		return CloneUtils.clone(productPriceSpecialStartDate);
	}

	public void setProductPriceSpecialStartDate(
			Date productPriceSpecialStartDate) {
		this.productPriceSpecialStartDate = CloneUtils.clone(productPriceSpecialStartDate);
	}

	public Date getProductPriceSpecialEndDate() {
		return CloneUtils.clone(productPriceSpecialEndDate);
	}

	public void setProductPriceSpecialEndDate(Date productPriceSpecialEndDate) {
		this.productPriceSpecialEndDate = CloneUtils.clone(productPriceSpecialEndDate);
	}

	public Integer getProductPriceSpecialDurationDays() {
		return productPriceSpecialDurationDays;
	}

	public void setProductPriceSpecialDurationDays(
			Integer productPriceSpecialDurationDays) {
		this.productPriceSpecialDurationDays = productPriceSpecialDurationDays;
	}

	public BigDecimal getProductPriceSpecialAmount() {
		return productPriceSpecialAmount;
	}

	public void setProductPriceSpecialAmount(
			BigDecimal productPriceSpecialAmount) {
		this.productPriceSpecialAmount = productPriceSpecialAmount;
	}



	public Set<ProductPriceDescription> getDescriptions() {
		return descriptions;
	}



	public void setDescriptions(Set<ProductPriceDescription> descriptions) {
		this.descriptions = descriptions;
	}



	public ProductAvailability getProductPriceAvailability() {
		return productPriceAvailability;
	}



	public void setProductPriceAvailability(ProductAvailability productPriceAvailability) {
		this.productPriceAvailability = productPriceAvailability;
	}

}
