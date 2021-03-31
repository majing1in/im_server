package com.xiaoma.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("private_chat_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateChatList {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String messageContent;
    private Integer messageType;
    private Date createTime;
}
