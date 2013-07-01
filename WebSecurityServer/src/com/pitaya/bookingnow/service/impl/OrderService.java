package com.pitaya.bookingnow.service.impl;

import java.util.List;

import com.pitaya.bookingnow.dao.OrderMapper;
import com.pitaya.bookingnow.dao.Order_Table_DetailMapper;
import com.pitaya.bookingnow.dao.TableMapper;
import com.pitaya.bookingnow.dao.security.UserMapper;
import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.entity.Order_Table_Detail;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.service.IOrderService;
import com.pitaya.bookingnow.util.Constants;

public class OrderService implements IOrderService{

	private OrderMapper orderDao;
	private UserMapper userDao;
	private Order_Table_DetailMapper table_DetailDao;
	private TableMapper tableDao;
	
	
	public TableMapper getTableDao() {
		return tableDao;
	}

	public void setTableDao(TableMapper tableDao) {
		this.tableDao = tableDao;
	}

	public UserMapper getUserDao() {
		return userDao;
	}

	public void setUserDao(UserMapper userDao) {
		this.userDao = userDao;
	}

	public Order_Table_DetailMapper getTable_DetailDao() {
		return table_DetailDao;
	}

	public void setTable_DetailDao(Order_Table_DetailMapper table_DetailDao) {
		this.table_DetailDao = table_DetailDao;
	}

	public OrderMapper getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderMapper orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public boolean add(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeOrderById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Order> searchOrders(Order order) {
		
		return orderDao.searchOrders(order);
	}

	@Override
	public boolean addWaitingOrder(Order order) {
		if (order.getUser_id() != null) {
			//check waiter info is exist in order
			
			List<Order_Table_Detail> table_Details = order.getTable_details();
			
			if (table_Details != null && table_Details.size() > 0) {
				for (int i = 0; i < table_Details.size(); i++) {
					Order_Table_Detail table_Detail = table_Details.get(i);
					if(table_Detail.getTable_id() != null){
						//check table info is exist in order
						
						
						orderDao.insertSelective(order);
						
						table_Detail.setOrder_id(order.getId());
						
						table_DetailDao.insertSelective(table_Detail);
						
						Table table = new Table();
						table.setId(table_Detail.getTable_id());
						table.setStatus(Constants.TABLE_USING);
						
						tableDao.updateByPrimaryKeySelective(table);
						
						
					}
				}
				return true;
			}
			
		}
		
		return false;
	}

	@Override
	public boolean addNewOrder(Order order) {
		if (order.getUser_id() != null) {
			//check waiter info is exist in order
			
			List<Order_Table_Detail> table_Details = order.getTable_details();
			
			if (table_Details != null && table_Details.size() > 0) {
				for (int i = 0; i < table_Details.size(); i++) {
					Order_Table_Detail table_Detail = table_Details.get(i);
					if(table_Detail.getTable_id() != null){
						//check table info is exist in order
						
						
						orderDao.insertSelective(order);
						
						table_Detail.setOrder_id(order.getId());
						
						table_DetailDao.insertSelective(table_Detail);
						
						Table table = new Table();
						table.setId(table_Detail.getTable_id());
						table.setStatus(Constants.TABLE_USING);
						
						tableDao.updateByPrimaryKeySelective(table);
						
						
					}
				}
				return true;
			}
			
		}
		return false;
	}

}
