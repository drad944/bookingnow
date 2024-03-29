package com.pitaya.bookingnow.util;

import java.util.List;

public class SearchParams {
	
	private Long user_id;
	private Long customer_id;
	private List<Integer> orderStatusList;
	private List<Integer> foodStatusList;
	private List<Integer> food_detailStatusList;
	private Long order_id;
	
	private Long food_detail_id;
	
	private Long table_id;
	
	private Integer customer_count;
	
	private Integer status;
	
	private Integer offset;
	
	private Integer rowCount;
	
	private Integer x;
	private Integer y;
	private Integer w;
	private Integer h;
	private Double scaleRatio;
	
	private String imagePath;
	
	private Long fromDateTime;
	private Long toDateTime;
	
	
	public Long getFromDateTime() {
		return fromDateTime;
	}
	public void setFromDateTime(Long fromDateTime) {
		this.fromDateTime = fromDateTime;
	}
	public Long getToDateTime() {
		return toDateTime;
	}
	public void setToDateTime(Long toDateTime) {
		this.toDateTime = toDateTime;
	}
	public Integer getW() {
		return w;
	}
	public void setW(Integer w) {
		this.w = w;
	}
	public Integer getH() {
		return h;
	}
	public void setH(Integer h) {
		this.h = h;
	}
	public Double getScaleRatio() {
		return scaleRatio;
	}
	public void setScaleRatio(Double scaleRatio) {
		this.scaleRatio = scaleRatio;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
	public List<Integer> getFood_detailStatusList() {
		return food_detailStatusList;
	}
	public void setFood_detailStatusList(List<Integer> food_detailStatusList) {
		this.food_detailStatusList = food_detailStatusList;
	}
	public List<Integer> getFoodStatusList() {
		return foodStatusList;
	}
	public void setFoodStatusList(List<Integer> foodStatusList) {
		this.foodStatusList = foodStatusList;
	}
	public Long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}
	public Long getFood_detail_id() {
		return food_detail_id;
	}
	public void setFood_detail_id(Long food_detail_id) {
		this.food_detail_id = food_detail_id;
	}
	public Long getTable_id() {
		return table_id;
	}
	public void setTable_id(Long table_id) {
		this.table_id = table_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCustomer_count() {
		return customer_count;
	}
	public void setCustomer_count(Integer customer_count) {
		this.customer_count = customer_count;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public Long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}
	public List<Integer> getOrderStatusList() {
		return orderStatusList;
	}
	public void setOrderStatusList(List<Integer> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}
	
	public void cleanup() {
		this.setCustomer_count(null);
		this.setCustomer_id(null);
		this.setFood_detail_id(null);
		this.setOrder_id(null);
		this.setOrderStatusList(null);
		this.setStatus(null);
		this.setTable_id(null);
		this.setUser_id(null);
	}
}
