package com.xiaoma.im.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedPackage {

    private String redPackageId;
    /** 1个人红包 2群聊红包 */
    private Integer redPackageType;
    private String redPackageOwner;
    private String redPackageMessage;
    private BigDecimal amount;
    private Integer count;
    private Date createTime;
}
