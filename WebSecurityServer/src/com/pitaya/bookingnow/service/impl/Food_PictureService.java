package com.pitaya.bookingnow.service.impl;

import java.util.List;

import com.pitaya.bookingnow.dao.Food_PictureMapper;
import com.pitaya.bookingnow.entity.Food_Picture;
import com.pitaya.bookingnow.service.IFood_PictureService;

public class Food_PictureService implements IFood_PictureService{

	private Food_PictureMapper food_pictureDao;

	public Food_PictureMapper getFood_pictureDao() {
		return food_pictureDao;
	}

	public void setFood_pictureDao(Food_PictureMapper food_pictureDao) {
		this.food_pictureDao = food_pictureDao;
	}

	@Override
	public int add(Food_Picture food_picture) {
		int result = food_pictureDao.insertSelective(food_picture);
		if (result > 0) {
			return result;
		}
		return result;
	}

	@Override
	public boolean removeFood_PictureById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Food_Picture food_picture) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(Food_Picture food_picture) {
		int result = food_pictureDao.updateByPrimaryKeySelective(food_picture);
		if(result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Food_Picture> searchFood_Pictures(Food_Picture food_picture) {
		List<Food_Picture> realFood_Pictures = food_pictureDao.searchFood_Pictures(food_picture);
		if (realFood_Pictures != null && realFood_Pictures.size() > 0) {
			for (int i = 0; i < realFood_Pictures.size(); i++) {
				realFood_Pictures.get(i).renderePicture();
			}
		}
		return realFood_Pictures;
	}

	@Override
	public Food_Picture searchPictureByFoodId(Long id) {
		Food_Picture realFood_Picture = food_pictureDao.selectByFoodId(id);
		if (realFood_Picture != null) {
			realFood_Picture.renderePicture();
		}
		return realFood_Picture;
	}

	@Override
	public List<Food_Picture> searchAllFood_Pictures() {
		List<Food_Picture> realFood_Pictures = food_pictureDao.searchAllFood_Pictures();
		if (realFood_Pictures != null && realFood_Pictures.size() > 0) {
			for (int i = 0; i < realFood_Pictures.size(); i++) {
				realFood_Pictures.get(i).renderePicture();
			}
		}
		return realFood_Pictures;
	}

	@Override
	public List<Food_Picture> searchFood_PicturesWithoutImage(
			Food_Picture food_picture) {
		return food_pictureDao.searchFood_PicturesWithoutImage(food_picture);
	}

	@Override
	public List<Food_Picture> searchAllFood_PicturesWithoutImage() {
		return food_pictureDao.searchAllFood_PicturesWithoutImage();
	}

	@Override
	public Food_Picture searchSmallPictureByFoodId(Long id) {
		Food_Picture realFood_Picture = food_pictureDao.selectSmallImageByFoodId(id);
		if (realFood_Picture != null) {
			realFood_Picture.renderePicture();
		}
		return realFood_Picture;
	}

	@Override
	public Food_Picture searchLargePictureByFoodId(Long id) {
		Food_Picture realFood_Picture = food_pictureDao.selectLargeImageByFoodId(id);
		if (realFood_Picture != null) {
			realFood_Picture.renderePicture();
		}
		return realFood_Picture;
	}


}
