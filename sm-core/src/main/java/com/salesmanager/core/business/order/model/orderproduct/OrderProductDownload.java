package com.salesmanager.core.business.order.model.orderproduct;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table (name="ORDER_PRODUCT_DOWNLOAD", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class OrderProductDownload implements Serializable {
	private static final long serialVersionUID = -8935511990745477240L;
	
	@Id
	@Column (name="ORDER_PRODUCT_DOWNLOAD_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ORDER_PRODUCT_DL_ID_NEXT_VALUE")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ORDER_PRODUCT_ID", nullable = false)
	private OrderProduct orderProduct; 

	@Column(name = "ORDER_PRODUCT_FILENAME", nullable = false)
	private String orderProductFilename;
	
	@Column(name = "DOWNLOAD_MAXDAYS", nullable = false)
	private Integer maxdays;
	
	@Column(name = "DOWNLOAD_COUNT", nullable = false)
	private Integer downloadCount;
	
	// TODO : Order : Replace by an object
	@Column(name = "FILE_ID", nullable = false)
	private long fileId;
	
	public OrderProductDownload() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderProduct getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(OrderProduct orderProduct) {
		this.orderProduct = orderProduct;
	}

	public String getOrderProductFilename() {
		return orderProductFilename;
	}

	public void setOrderProductFilename(String orderProductFilename) {
		this.orderProductFilename = orderProductFilename;
	}

	public Integer getMaxdays() {
		return maxdays;
	}

	public void setMaxdays(Integer maxdays) {
		this.maxdays = maxdays;
	}

	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
}