package com.pitaya.bookingnow.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Food {
    private Long id;

    private Long picture_id;

    private String name;

    private Integer version;

    private Date period;

    private BigDecimal price;

    private Integer category;

    private String description;
    
    private Food_Picture picture;
    
    private List<Food_Material_Detail> material_details; 

    public Food_Picture getPicture() {
		return picture;
	}

	public void setPicture(Food_Picture picture) {
		this.picture = picture;
	}

	public List<Food_Material_Detail> getMaterial_details() {
		return material_details;
	}

	public void setMaterial_details(List<Food_Material_Detail> material_details) {
		this.material_details = material_details;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(Long picture_id) {
        this.picture_id = picture_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}