package com.pitaya.bookingnow.service.impl.security;
import java.util.List;


import com.pitaya.bookingnow.dao.security.ResourceMapper;
import com.pitaya.bookingnow.entity.security.Resource;
import com.pitaya.bookingnow.service.security.IResourceService;

public class ResourceService implements IResourceService {

	private ResourceMapper resourceDao;
	
	public ResourceMapper getResourceDao() {
		return resourceDao;
	}

	public void setResourceDao(ResourceMapper resourceDao) {
		this.resourceDao = resourceDao;
	}

	@Override
	public boolean add(Resource resource) {
		if(resourceDao.insertSelective(resource) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeResourceById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Resource resource) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(Resource resource) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Resource> searchResources(Resource resource) {
		// TODO Auto-generated method stub
		return null;
	}



}