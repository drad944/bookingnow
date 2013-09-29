package com.pitaya.bookingnow.entity.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

public class User {
	public User() {
		
	}
	private static Log logger =  LogFactory.getLog(User.class);
	
    private Long id;
    
    private Long modifyTime;
    
    private byte[] image;
    
    private Integer image_size;
    
	private String image_relative_path;
	
    private String image_absolute_path;
    
    private String account;

    private String name;

    private String password;

    private String phone;

    private Integer sex;

    private String email;

    private String address;

    private Long birthday;

    private String description;

    private Integer department;

    private Integer sub_system;
    
    private Boolean enabled;
    
    List<User_Role_Detail> role_Details;

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getImage_size() {
		return image_size;
	}

	public void setImage_size(Integer image_size) {
		this.image_size = image_size;
	}

	public String getImage_relative_path() {
		return image_relative_path;
	}

	public void setImage_relative_path(String image_relative_path) {
		this.image_relative_path = image_relative_path;
	}

	public String getImage_absolute_path() {
		return image_absolute_path;
	}

	public void setImage_absolute_path(String image_absolute_path) {
		this.image_absolute_path = image_absolute_path;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<User_Role_Detail> getRole_Details() {
		return role_Details;
	}

	public void setRole_Details(List<User_Role_Detail> role_Details) {
		this.role_Details = role_Details;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Integer getSub_system() {
        return sub_system;
    }

    public void setSub_system(Integer sub_system) {
        this.sub_system = sub_system;
    }
    
    public void parseImageSize() {
    	File file = null;
    	
    	String pathprefix = ServletActionContext.getServletContext().getRealPath("/");
    	if (this.getImage_relative_path() != null && this.getImage_relative_path().length() > 0) {
    		file = new File(pathprefix + this.getImage_relative_path());
			if (file.exists()) {
				this.setImage_size((int) file.length());
				file = null;
			}else {
				logger.info(file.toString() + " -------------------- large picture is not exist.");
			}
		}
    }
    
    public void renderePicture() {
    	FileInputStream fis = null;
    	File file = null;
    	
    	String pathprefix = ServletActionContext.getServletContext().getRealPath("/");
    	if (this.getImage_relative_path() != null && this.getImage_relative_path().length() > 0) {
			
    		file = new File(pathprefix + this.getImage_relative_path());
			
			if (file.exists()) {
				try {
					fis = new FileInputStream(file);
					byte[] buffer = new byte[1024];
					byte[] pictureImage = new byte[(int) file.length()];
					
					int len = 0;
					int startIndex = 0;
					while((len = fis.read(buffer)) > 0) {
						
						System.arraycopy(buffer, 0, pictureImage, startIndex, len);
						startIndex = startIndex + len;
					}
					if (this.getImage_size() == null || this.getImage_size() <= 0) {
						this.setImage_size((int) file.length());
					}
					this.setImage(pictureImage);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}else {
				logger.info(file.toString() + " -------------------- large picture is not exist.");
			}
		}
    	
    }
}