package com.jingmi.chat.common.common.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @program: jingmiChat
 * @description:
 * @author: JingMi
 * @create: 2024-07-21 00:10
 **/
@Data
@Builder
public class RequestInfo {
    private Long uid;
    private String ip;
}
