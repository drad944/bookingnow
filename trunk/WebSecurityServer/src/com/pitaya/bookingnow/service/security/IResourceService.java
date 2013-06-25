package com.pitaya.bookingnow.service.security;




import java.util.List;

import com.pitaya.bookingnow.entity.security.Resource;





public interface IResourceService{
	boolean add(Resource resource);
	
	boolean removeResourceById(Long id);
	
	boolean remove(Resource resource);

	boolean modify(Resource resource);
	
	List<Resource> searchResources(Resource resource);

}
