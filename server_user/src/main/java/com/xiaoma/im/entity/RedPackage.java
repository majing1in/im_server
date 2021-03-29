package com.xiaoma.im.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedPackage implements Serializable {

    private static final long serialVersionUID = 5610831101139693314L;

    private String redPackageId;
    /** 14个人红包 15群聊红包 */
    private Integer redPackageType;
    private String redPackageOwner;
    private String redPackageMessage;
    private BigDecimal amount;
    private Integer count;
    private Date createTime;
    private List<Integer> list;
}
