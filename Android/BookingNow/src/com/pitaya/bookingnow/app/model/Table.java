package com.pitaya.bookingnow.app.model;

import java.io.Serializable;

public class Table implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -716364574954133396L;
	private String label;
	private Long id;
	private int mincount;
	private int maxcount;
	
	public Table(){
	}
	
	public Table(Long id, String label){
		this.id = id;
		this.label = label;
	}
	
	public Long getId(){
		return this.id;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public int getMinCount(){
		return this.mincount;
	}
	
	public int getMaxCount(){
		return this.maxcount;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public void setMinCount(int count){
		this.mincount = count;
	}
	
	public void setMaxCount(int count){
		this.maxcount = count;
	}
	
}
