package com.salesmanager.core.business.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Type;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.constants.SchemaConstant;

/**
 * Merchant configuration information
 * @author casams1
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MERCHANT_CONFIGURATION", schema= SchemaConstant.SALESMANAGER_SCHEMA)
public class MerchantConfiguration extends SalesManagerEntity<Long, MerchantConfiguration> implements Serializable, Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4246917986731953459L;

	@Id
	@Column(name = "MERCHANT_CONFIG_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MERCH_CONF_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MERCHANT_ID", nullable=true)
	private MerchantStore merchantSore;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@Column(name="KEY")
	private String key;
	
	@Column(name="VALUE")
	@Type(type = "org.hibernate.type.StringClobType")
	private String value;

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}



	public MerchantStore getMerchantSore() {
		return merchantSore;
	}

	public void setMerchantSore(MerchantStore merchantSore) {
		this.merchantSore = merchantSore;
	}
}
