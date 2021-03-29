package com.xiaoma.im.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoma.im.entity.UserMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMoneyMapper extends BaseMapper<UserMoney> {

    /**
     * 获取用户金额
     * @param sqlStr
     * @return
     */
    @Select("${sqlStr}")
    UserMoney getUserMoney(@Param(value = "sqlStr") String sqlStr);
}
