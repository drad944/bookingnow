package com.pitaya.bookingnow.entity.security;

import java.util.List;

public class Role {
    private Long id;

    private String name;

    private String description;

    private Boolean issys;

    private Integer module;
    
    private List<Role_Authority_Detail> authority_details;


    public List<Role_Authority_Detail> getAuthority_details() {
		return authority_details;
	}

	public void setAuthority_details(List<Role_Authority_Detail> authority_details) {
		this.authority_details = authority_details;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIssys() {
        return issys;
    }

    public void setIssys(Boolean issys) {
        this.issys = issys;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }
}