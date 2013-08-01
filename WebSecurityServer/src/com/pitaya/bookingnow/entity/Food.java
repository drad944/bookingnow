package com.pitaya.bookingnow.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

public class Food {
	private static Log logger =  LogFactory.getLog(Food.class);
	
    private Long id;

    private Integer recommendation;

    private Integer status;


    private String name;

    private Long version;
    
    private Long image_version;

    private Long period;

    private Double price;

    private String category;

    private String description;
    
    
    private Boolean enabled;
    
    private String large_image_relative_path;

    private String small_image_relative_path;
    
    private String large_image_absolute_path;

    private String small_image_absolute_path;
    
    private Integer large_image_size;
    
    private Integer small_image_size;
    
    private byte[] small_image;
    
    private byte[] large_image;
    
    public Long getImage_version() {
		return image_version;
	}

	public void setImage_version(Long image_version) {
		this.image_version = image_version;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getLarge_image_relative_path() {
		return large_image_relative_path;
	}

	public void setLarge_image_relative_path(String large_image_relative_path) {
		this.large_image_relative_path = large_image_relative_path;
	}

	public String getSmall_image_relative_path() {
		return small_image_relative_path;
	}

	public void setSmall_image_relative_path(String small_image_relative_path) {
		this.small_image_relative_path = small_image_relative_path;
	}

	public String getLarge_image_absolute_path() {
		return large_image_absolute_path;
	}

	public void setLarge_image_absolute_path(String large_image_absolute_path) {
		this.large_image_absolute_path = large_image_absolute_path;
	}

	public String getSmall_image_absolute_path() {
		return small_image_absolute_path;
	}

	public void setSmall_image_absolute_path(String small_image_absolute_path) {
		this.small_image_absolute_path = small_image_absolute_path;
	}

	public Integer getLarge_image_size() {
		return large_image_size;
	}

	public void setLarge_image_size(Integer large_image_size) {
		this.large_image_size = large_image_size;
	}

	public Integer getSmall_image_size() {
		return small_image_size;
	}

	public void setSmall_image_size(Integer small_image_size) {
		this.small_image_size = small_image_size;
	}

	public byte[] getSmall_image() {
		return small_image;
	}

	public void setSmall_image(byte[] small_image) {
		this.small_image = small_image;
	}

	public byte[] getLarge_image() {
		return large_image;
	}

	public void setLarge_image(byte[] large_image) {
		this.large_image = large_image;
	}

	public Double getPrice() {
		return price;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Integer recommendation) {
        this.recommendation = recommendation;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }


    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void renderePicture() {
    	FileInputStream fis = null;
    	File file = null;
    	
    	String pathprefix = ServletActionContext.getServletContext().getRealPath("/");
    	if (this.getLarge_image_relative_path() != null && this.getLarge_image_relative_path().length() > 0) {
			
    		file = new File(pathprefix + this.getLarge_image_relative_path());
			
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
					if (this.getLarge_image_size() == null || this.getLarge_image_size() <= 0) {
						this.setLarge_image_size((int) file.length());
					}
					this.setLarge_image(pictureImage);
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
    	
    	if (this.getSmall_image_relative_path() != null && this.getSmall_image_relative_path().length() > 0) {
			file = new File(pathprefix + this.getSmall_image_relative_path());
			
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
					if (this.getSmall_image_size() == null || this.getSmall_image_size() <= 0) {
						this.setSmall_image_size((int) file.length());
					}
					this.setSmall_image(pictureImage);
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
				logger.info(file.toString() + " -------------------- small picture is not exist.");
			}
		}
    }
}