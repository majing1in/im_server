package com.xiaoma.im.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoma.im.entity.UserDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserDetails> {

}