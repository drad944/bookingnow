package com.pitaya.bookingnow.mapper;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface BaseSqlMapper<T> extends SqlMapper {   
	public void add(T model) throws DataAccessException;   
	public void edit(T model) throws DataAccessException;   
	public void remove(T model) throws DataAccessException;   
	public T get(T model) throws DataAccessException;   
	public List<T> getList(T model) throws DataAccessException; 
}
