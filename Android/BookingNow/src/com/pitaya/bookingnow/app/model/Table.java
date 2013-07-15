package com.pitaya.bookingnow.app.model;

public class Table {
	
	private String label;
	private Long id;
	
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
	
}
