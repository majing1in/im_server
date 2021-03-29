package com.xiaoma.im.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@TableName("user_money")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMoney {

    @TableId
    private Integer id;
    private Integer uid;
    private BigDecimal money;
    @Version
    private Integer version;
}