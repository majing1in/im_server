package com.xiaoma.im.vo;

import com.xiaoma.im.entity.PointToPoint;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author Xiaoma
 * @Date 2021/2/16 0016 12:23
 * @Email 1468835254@qq.com
 */
@Getter
@Setter
@Builder
public class PointToPointVo extends PointToPoint implements Serializable {

    private static final long serialVersionUID = 8050477961361618803L;
    private int commandType;

    private String userAccount;

    private String friendAccount;
}
