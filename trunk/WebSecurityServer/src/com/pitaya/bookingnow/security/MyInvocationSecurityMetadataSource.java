package com.pitaya.bookingnow.security;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import com.pitaya.bookingnow.dao.security.AuthorityMapper;
import com.pitaya.bookingnow.entity.security.Role;
import com.pitaya.bookingnow.service.security.IAuthorityService;
import com.pitaya.bookingnow.service.security.IResourceService;


public class MyInvocationSecurityMetadataSource
		implements FilterInvocationSecurityMetadataSource {
	private AntPathMatcher urlMatcher = new AntPathMatcher();
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	
    @Resource
    private IResourceService resourceService;
    
	@Resource
    private IAuthorityService authorityService;
	

	public IResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(IResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public IAuthorityService getAuthorityService() {
		return authorityService;
	}

	public void setAuthorityService(IAuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	public MyInvocationSecurityMetadataSource() {
		loadResourceDefine();
	}
	
	private void loadResourceDefine() {
		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
		ConfigAttribute ca = new SecurityConfig("ROLE_ADMIN");
		atts.add(ca);
		resourceMap.put("/index.jsp", atts);
		resourceMap.put("/i.jap", atts);
	}
	
	private void loadResourceDefine1() {
		SqlSessionFactory sqlSessionFactory = null;
		String resource = "mybatis-config.xml";
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(resource);
		} catch (IOException e) {
			System.out.println(e.getMessage());

		}
		/*
		char[] cbuf = new char[1024];
		try {
			reader.read(cbuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

		SqlSession sqlSession = sqlSessionFactory.openSession();
		AuthorityMapper authorityEntityMapper = sqlSession
				.getMapper(AuthorityMapper.class);
		//List<String> query = authorityEntityMapper.selectAllAuthoritiesName();

		List<String> query = new ArrayList<>();
		query.add("ROLE_USER");
		query.add("ROLE_ADMIN");
		
		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();

		for (String auth : query) {
			ConfigAttribute ca = new SecurityConfig(auth);

			List<String> query1 = sqlSession.selectList(
							"com.pitaya.bookingnow.dao.security.AuthorityMapper.selectResourceNamebyAuthoritiesName",	auth);

			for (String res : query1) {
				String url = res;

				/*
				 * �ж���Դ�ļ���Ȩ�޵Ķ�Ӧ��ϵ������Ѿ�������ص���Դurl����Ҫͨ���urlΪkey��ȡ��Ȩ�޼��ϣ���Ȩ�����ӵ�Ȩ�޼����С�
				 * sparta
				 */
				if (resourceMap.containsKey(url)) {

					Collection<ConfigAttribute> value = resourceMap.get(url);
					value.add(ca);
					resourceMap.put(url, value);
				} else {
					Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
					atts.add(ca);
					resourceMap.put(url, atts);
				}

			}

		}

	}

	// According to a URL, Find out permission configuration of this URL.
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		// guess object is a URL which user request.
		String url = ((FilterInvocation)object).getRequestUrl();
		
		if("/".equals(url)){
	           return null;
	    }
		
		int firstQuestionMarkIndex = url.indexOf(".");
        //�ж������Ƿ���в��� ����в����ȥ������ĺ�׺�Ͳ���(/index.do  --> /index)
        if(firstQuestionMarkIndex != -1){
            url = url.substring(0,firstQuestionMarkIndex);
        }
        
        if(resourceMap == null){
        		return null;
        }
		
		Iterator<String> ite = resourceMap.keySet().iterator();
		while (ite.hasNext()) {
			String resURL = ite.next();
			if (urlMatcher.match(url, resURL)) {
				return resourceMap.get(resURL);
			}
		}
		return null;
	}
	
    private Collection<ConfigAttribute> listToCollection(List<Role> roles) {
        List<ConfigAttribute> list = new ArrayList<ConfigAttribute>();

        for (Role role : roles) {
            list.add(new SecurityConfig(role.getName()));

        }
        return list;
    }

	public boolean supports(Class<?> clazz) {
		return true;
	}
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

}