package com.pitaya.bookingnow.service.impl;

import com.pitaya.bookingnow.dao.DiningTableMapper;
import com.pitaya.bookingnow.model.DiningTable;
import com.pitaya.bookingnow.service.IDiningTableService;

public class DiningTableService implements IDiningTableService{

	private DiningTableMapper diningTableDao;
	
	public DiningTableMapper getDiningTableDao() {
		return diningTableDao;
	}

	public void setDiningTableDao(DiningTableMapper diningTableDao) {
		this.diningTableDao = diningTableDao;
	}

	@Override
	public int deleteByPrimaryKey(Integer tid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(DiningTable record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(DiningTable record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DiningTable selectByPrimaryKey(Integer tid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(DiningTable record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(DiningTable record) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
