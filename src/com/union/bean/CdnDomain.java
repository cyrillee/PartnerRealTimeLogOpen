package com.union.bean;


public class CdnDomain implements java.io.Serializable {

	private Integer domainId;
	private String domain;
	private String dbSchemas;
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

}