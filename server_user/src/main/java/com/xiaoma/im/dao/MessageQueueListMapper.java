package com.xiaoma.im.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoma.im.entity.MessageQueueList;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageQueueListMapper extends BaseMapper<MessageQueueList> {

    /**
     * 保存
     */
    @Insert("${sqlStr}")
    int insert(@Param(value = "sqlStr") String sqlStr);

    /**
     * 根据transactionNo 统计条数
     */
    @Select("${sqlStr}")
    long countByTransactionNo(@Param(value = "sqlStr") String sqlStr);
}