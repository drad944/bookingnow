package com.pitaya.bookingnow.service;

import java.util.List;


public interface IBaseService <T>{
	public boolean add(T entity);
	public boolean delete(T entity);
	public boolean modify(T entity);
	public T get(T entity);
	public T getByID(T entity);
	public List<T> getAll(T entity);
}
