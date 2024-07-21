package com.jingmi.chat.common.user.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: jingmiChat
 * @description: 修改名字请求
 * @author: JingMi
 * @create: 2024-07-21 11:37
 **/
@Data
public class ModifyNameReq {
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 6,message = "用户名长度不能超过6个字符")
    private String name;
    @NotNull(message = "用户id不能为空")
    private Integer uid;
}
