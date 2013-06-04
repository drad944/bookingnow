package com.pitaya.bookingnow.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;

public interface BaseMapper<T>{
	
	public boolean add(T entity) throws DataAccessException;
	public boolean delete(T entity) throws DataAccessException;
	public boolean modify(T entity) throws DataAccessException;
	public T get(T entity) throws DataAccessException;
	public T getByID(T entity) throws DataAccessException;
	public List<T> getAll(T entity) throws DataAccessException;
}
