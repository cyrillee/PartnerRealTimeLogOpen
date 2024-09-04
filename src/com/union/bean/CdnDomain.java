package com.union.bean;


public class CdnDomain implements java.io.Serializable {

	private Integer domainId;
	private String domain;
	private String dbSchemas;
	private String supply;
	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getDbSchemas() {
		return dbSchemas;
	}
	public void setDbSchemas(String dbSchemas) {
		this.dbSchemas = dbSchemas;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public String getSupply() {
		return supply;
	}

	public void setSupply(String supply) {
		this.supply = supply;
	}
}