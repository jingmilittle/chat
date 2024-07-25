package com.jingmi.chat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: jingmiChat
 * @description: 佩戴徽章的req
 * @author: JingMi
 * @create: 2024-07-23 23:12
 **/
@Data
public class WearingBadgeReq {

    @NotNull(message = "徽章id为空")
    @ApiModelProperty("徽章id")
    private Long itemId;



}
