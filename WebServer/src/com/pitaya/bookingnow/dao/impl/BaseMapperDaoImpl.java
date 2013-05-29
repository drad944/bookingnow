package com.pitaya.bookingnow.dao.impl;

import java.util.List; 

import org.apache.ibatis.session.SqlSessionFactory; 
import org.mybatis.spring.SqlSessionTemplate; 
import org.springframework.stereotype.Repository; 

import com.pitaya.bookingnow.dao.BaseMapperDao;
import com.pitaya.bookingnow.mapper.BaseSqlMapper;


@SuppressWarnings("unchecked")
@Repository
public class BaseMapperDaoImpl<T> extends SqlSessionTemplate implements	BaseMapperDao<T> {
	
	private Class<? extends BaseSqlMapper> mapperClass;
	

	public BaseMapperDaoImpl(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}


	public void setMapperClass(Class<? extends BaseSqlMapper> mapperClass) {
		this.mapperClass = mapperClass;
	}

	public BaseSqlMapper<T> getMapper() {
		return this.getMapper(mapperClass);
	}

	public boolean add(T entity) throws Exception {
		boolean flag = false;
		try {
			this.getMapper().add(entity);
			flag = true;
		} catch (Exception e) {
			flag = false;
			throw e;
		}
		return flag;
	}

	public boolean edit(T entity) throws Exception {
		boolean flag = false;
		try {
			this.getMapper().edit(entity);
			flag = true;
		} catch (Exception e) {
			flag = false;
			throw e;
		}
		return flag;
	}

	public T get(T entity) throws Exception {
		return this.getMapper().get(entity);
	}

	public List<T> getAll() throws Exception {
		return this.getMapper().getList(null);
	}

	public boolean remove(T entity) throws Exception {
		boolean flag = false;
		try {

			this.getMapper().remove(entity);

			flag = true;
		} catch (Exception e) {
			flag = false;
			throw e;
		}
		return flag;
	}

	@Override
	public List<T> getAll(T entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}