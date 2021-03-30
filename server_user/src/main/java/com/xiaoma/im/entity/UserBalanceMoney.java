package com.xiaoma.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("user_balance_money")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceMoney implements Serializable {

    private static final long serialVersionUID = 8717937671321621057L;
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private BigDecimal money;
    @Version
    private Integer version;
}
