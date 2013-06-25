package com.pitaya.bookingnow.dao.security;

import com.pitaya.bookingnow.entity.security.User_Picture;

public interface User_PictureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User_Picture record);

    int insertSelective(User_Picture record);

    User_Picture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User_Picture record);

    int updateByPrimaryKeyWithBLOBs(User_Picture record);

    int updateByPrimaryKey(User_Picture record);
}