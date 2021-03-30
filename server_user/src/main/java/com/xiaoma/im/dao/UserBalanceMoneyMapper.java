package com.xiaoma.im.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoma.im.entity.UserBalanceMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserBalanceMoneyMapper extends BaseMapper<UserBalanceMoney> {

    /**
     * 获取用户金额
     * @param sqlStr
     * @return
     */
    @Select("${sqlStr}")
    UserBalanceMoney getUserMoney(@Param(value = "sqlStr") String sqlStr);
}
