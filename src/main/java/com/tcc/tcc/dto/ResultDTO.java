package com.tcc.tcc.dto;

import java.io.Serializable;

public class ResultDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fields;
	private String filters;
	
	public ResultDTO() {
		super();
	}

	public ResultDTO(String fields, String filters) {
		super();
		this.fields = fields;
		this.filters = filters;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}	
}
