package com.xiaoma.im.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointRedEnvelope {

    private String uuid;
    private String type;
    private String userAccount;
    private String message;
    private String amount;
    private Date createTime;
}
