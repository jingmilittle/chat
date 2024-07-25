package com.jingmi.chat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @program: jingmiChat
 * @description: 用户信息响应vo
 * @author: JingMi
 * @create: 2024-07-20 15:44
 **/
@Data
@Builder
public class UserInfoResp {
    @ApiModelProperty(value = "用户id")
    private Long id;
    @ApiModelProperty(value = "用户名字")
    private String name;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    @ApiModelProperty(value = "性别")
    private Integer sex;
    @ApiModelProperty(value = "改名卡次数")
    private Integer modifyNameChance;

}
