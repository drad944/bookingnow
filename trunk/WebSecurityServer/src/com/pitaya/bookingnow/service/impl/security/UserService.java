package com.pitaya.bookingnow.service.impl.security;
import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.pitaya.bookingnow.dao.security.RoleMapper;
import com.pitaya.bookingnow.dao.security.UserMapper;
import com.pitaya.bookingnow.dao.security.User_Role_DetailMapper;
import com.pitaya.bookingnow.entity.security.Role;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.entity.security.User_Role_Detail;
import com.pitaya.bookingnow.service.security.IUserService;
import com.pitaya.bookingnow.util.ImageUtil;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;
import com.pitaya.bookingnow.util.SystemUtils;

public class UserService implements IUserService {

	private UserMapper userDao;
	private User_Role_DetailMapper role_detailDao;
	
	public User_Role_DetailMapper getRole_detailDao() {
		return role_detailDao;
	}

	public void setRole_detailDao(User_Role_DetailMapper role_detailDao) {
		this.role_detailDao = role_detailDao;
	}

	private RoleMapper roleDao;
	
	public RoleMapper getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleMapper roleDao) {
		this.roleDao = roleDao;
	}


	public UserMapper getUserDao() {
		return userDao;
	}

	public void setUserDao(UserMapper userDao) {
		this.userDao = userDao;
	}

	@Override
	public MyResult add(User user) {
		MyResult result = new MyResult();
		if (user != null && user.getAccount() != null) {
			if (existUser(user)) {
				result.getErrorDetails().put("user_exist", "user have been registered.");
			}else {
				if (user.getModifyTime() == null) {
					user.setModifyTime(new Date().getTime());
				}
				if (userDao.insert(user) == 1) {
					result.setExecuteResult(true);
					result.setUser(userDao.selectByPrimaryKey(user.getId()));
					return result;
				}else {
					throw new RuntimeException("fail to insert user to DB.");
				}
			}
			
		}else {
			result.getErrorDetails().put("user_exist", "can not find user info in client data.");
		}
		return result;
	}

	@Override
	public MyResult removeUserById(Long id) {
		MyResult result = new MyResult();
		if (id != null) {
			if (userDao.deleteByPrimaryKey(id) == 1) {
				
				result.setExecuteResult(true);
				return result;
			}else {
				throw new RuntimeException("fail to delete user in DB.");
			}
		}else {
			result.getErrorDetails().put("user_exist", "can not find user id in client data.");
		}
		return result;
	}

	@Override
	public MyResult modify(User user) {
		MyResult result = new MyResult();
		if (user != null && user.getId() != null) {
			if (user.getModifyTime() == null) {
				user.setModifyTime(new Date().getTime());
			}
			if (userDao.updateByPrimaryKeySelective(user) == 1) {
				
				result.setExecuteResult(true);
				result.setUser(userDao.selectByPrimaryKey(user.getId()));
				return result;
			}else {
				throw new RuntimeException("fail to update user info in DB.");
			}
		}else {
			result.getErrorDetails().put("user_exist", "can not find user id in client data.");
		}
		return result;
	}

	@Override
	public List<User> searchUsers(User user) {
		MyResult result = new MyResult();
		if (user != null) {
			return userDao.searchUsers(user);
		}else {
			result.getErrorDetails().put("user_exist", "can not find user in client data.");
		}
		return null;
	}

	@Override
	public MyResult login(User user) {
		MyResult result = new MyResult();
		if (user != null) {
			User loginUser = userDao.login(user);
			if (loginUser != null && loginUser.getId() != null) {
				if(loginUser.getRole_Details() != null && loginUser.getRole_Details().size() > 0){
					result.setExecuteResult(true);
					result.setUser(loginUser);
				} else {
					result.getErrorDetails().put("Error", "该用户尚未分配角色");
				}
			}else {
				result.getErrorDetails().put("Error", "用户名或密码错误");
			}
		} else {
			result.getErrorDetails().put("Error", "未知错误");
		}
		return result;
	}

	@Override
	public User searchUserById(Long id) {
		if (id != null) {
			return userDao.selectByPrimaryKey(id);
		}
		return null;
	}

	@Override
	public List<User> searchUsersWithRole(User user) {
		MyResult result = new MyResult();
		if (user != null) {
			return userDao.searchUsersWithRole(user);
		}else {
			result.getErrorDetails().put("user_exist", "can not find user in client data.");
		}
		return null;
	}

	@Override
	public List<User> searchAllUsers() {
		return userDao.searchAllUsers();
	}

	@Override
	public User getUserRole(Long id) {
		if(id != null){
			return userDao.getUserRole(id);
		}
		return null;
	}

	@Override
	public MyResult cropPicture(SearchParams params) {
		MyResult result = new MyResult();
		if (params != null) {
			if (params.getUser_id() != null 
					&& params.getImagePath() != null
					&& params.getX() != null
					&& params.getY() != null
					&& params.getW() != null
					&& params.getH() != null) {
				User realUser = userDao.selectByPrimaryKey(params.getUser_id());
				if (realUser != null 
						&& realUser.getImage_relative_path() != null) {
					
					File dBFile = new File(realUser.getImage_relative_path());
					File clientFile = new File(params.getImagePath());
					if (dBFile.getPath().equals(clientFile.getPath())) {
						System.out.println(dBFile.getPath());
						String imageType = ImageUtil.parseImageType(params.getImagePath());
						String targetImagePath = ImageUtil.generateImagePath("images" + SystemUtils.getSystemDelimiter() + "user" + SystemUtils.getSystemDelimiter(), imageType);
						String pathprefix = ServletActionContext.getServletContext().getRealPath("/");
						if (ImageUtil.cut(pathprefix + params.getImagePath(), imageType, pathprefix + targetImagePath, params.getX(), params.getY(), params.getW(), params.getH())) {
							
							
							User newUser = new User();
							newUser.setId(realUser.getId());
							newUser.setImage_relative_path(targetImagePath);
							newUser.setImage_absolute_path(pathprefix + targetImagePath);
							newUser.parseImageSize();
							
							if (userDao.updateByPrimaryKeySelective(newUser) == 1) {
								result.setExecuteResult(true);
								result.setUser(userDao.selectByPrimaryKey(newUser.getId()));
								
								return result;
							}else {
								throw new RuntimeException("can not update user info in DB");
							}
							
						}else {
							
							result.getErrorDetails().put("crop_error", "can not crop image in server side");
						}
					}else {
						System.out.println(dBFile.getPath());
						result.getErrorDetails().put("image_exist", "image path in DB data is not the same with client data.");
					}
					
				}else {
					result.getErrorDetails().put("image_exist", "can not find image path in DB data.");
				}
				
			}else {
				result.getErrorDetails().put("params_exist", "params is not enough in client data.");
			}
		}else {
			result.getErrorDetails().put("params_exist", "can not find params in client data.");
		}
		
		return result;
	}

	@Override
	public boolean existUser(User user) {
		if (user != null && user.getAccount() != null) {
			User existedUser = userDao.existUser(user);
			if (existedUser != null && existedUser.getId() != null) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public MyResult modifyRoleWithUser(User user) {
		MyResult result = new MyResult();
		if (user != null && user.getId() != null) {
			User realUser = userDao.getUserRole(user.getId());
			if (realUser != null && realUser.getId() != null) {
				if (user.getRole_Details() != null && user.getRole_Details().size() > 0) {
					if (realUser.getRole_Details() != null && realUser.getRole_Details().size() > 0) {
						for (int i = realUser.getRole_Details().size() - 1; i >= 0; i--) {
							User_Role_Detail realRoleDetail = realUser.getRole_Details().get(i);
							for (int j = user.getRole_Details().size() - 1; j >= 0; j--) {
								Role tempRole = user.getRole_Details().get(j).getRole();
								if (tempRole.getName() != null || tempRole.getType() != null) {
									List<Role> tempDBRoles = roleDao.searchRoles(tempRole);
									if (tempDBRoles != null && tempDBRoles.size() > 0) {
										tempRole.setId(tempDBRoles.get(0).getId());
										if (tempDBRoles.get(0).getType().equals(realRoleDetail.getRole().getType()) || 
												tempDBRoles.get(0).getName().equals(realRoleDetail.getRole().getName())) {
											//do nothing for role_detail which exist in client and DB side.
											realUser.getRole_Details().remove(i);
											user.getRole_Details().remove(j);
										}
									}
								}
							}
						}
						//the remain in user.getRole_Details() should be of new role detail 
						for (int i = user.getRole_Details().size() - 1; i >= 0; i--) {
							User_Role_Detail tempRoleDetail = new User_Role_Detail();
							tempRoleDetail.setEnabled(true);
							tempRoleDetail.setRole_id(user.getRole_Details().get(i).getId());
							tempRoleDetail.setUser_id(user.getId());
							if (role_detailDao.insertSelective(tempRoleDetail) == 1) {
								
							}else {
								throw new RuntimeException("fail to insert user detail info in DB.");
							}
						}
						
						//the remain in realUser.getRole_Details() should be of delete role detail
						
						for (int i = realUser.getRole_Details().size() - 1; i >= 0; i--) {
							if (role_detailDao.deleteByPrimaryKey(realUser.getRole_Details().get(i).getId()) == 1) {
								
							}else {
								throw new RuntimeException("fail to delete user detail info in DB.");
							}
						}
					}else {
						//insert all user role detail
						for (int i = 0; i < user.getRole_Details().size(); i++) {
							Role tempRole = user.getRole_Details().get(i).getRole();
							if (tempRole.getName() != null || tempRole.getType() != null) {
								List<Role> realRoles = roleDao.searchRoles(tempRole);
								if (realRoles != null && realRoles.size() > 0) {
									User_Role_Detail tempRoleDetail = new User_Role_Detail();
									tempRoleDetail.setEnabled(true);
									tempRoleDetail.setRole_id(realRoles.get(0).getId());
									tempRoleDetail.setUser_id(user.getId());
									if (role_detailDao.insertSelective(tempRoleDetail) == 1) {
										
									}else {
										throw new RuntimeException("fail to insert user detail info in DB.");
									}
								}
							}
						}
					}
				}else {
					//remove all role_details for user.
					if (realUser.getRole_Details() != null && realUser.getRole_Details().size() > 0) {
						for (int i = 0; i < realUser.getRole_Details().size(); i++) {
							if (realUser.getRole_Details().get(i).getRole() != null) {
								if (role_detailDao.deleteByPrimaryKey(realUser.getRole_Details().get(i).getId()) == 1) {
									
								}else {
									throw new RuntimeException("fail to delete user detail info in DB.");
								}
							}
						}
					}else {
						//do nothing for user role.
					}
				}
			}else {
				result.getErrorDetails().put("user_exist", "can not find user id in DB data.");
			}
			
			
			if (user.getModifyTime() == null) {
				user.setModifyTime(new Date().getTime());
			}
			if (userDao.updateByPrimaryKeySelective(user) == 1) {
				
				result.setExecuteResult(true);
				result.setUser(userDao.getUserRole(user.getId()));
				return result;
			}else {
				throw new RuntimeException("fail to update user info in DB.");
			}
		}else {
			result.getErrorDetails().put("user_exist", "can not find user id in client data.");
		}
		return result;
	}
	
	


}