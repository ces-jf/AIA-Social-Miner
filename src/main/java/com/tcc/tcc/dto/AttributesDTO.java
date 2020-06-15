package com.tcc.tcc.dto;

import java.io.Serializable;

public class AttributesDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private boolean use;
	private String name;
	private String condition;
	
	public AttributesDTO() {
		super();
	}

	public AttributesDTO(int id, boolean use, String name, String condition) {
		super();
		this.id = id;
		this.use = use;
		this.name = name;
		this.condition = condition;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
