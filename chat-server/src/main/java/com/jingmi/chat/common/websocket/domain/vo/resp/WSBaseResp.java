package com.jingmi.chat.common.websocket.domain.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: jingmiChat
 * @description: 返回resp
 * @author: JingMi
 * @create: 2024-07-13 08:50
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSBaseResp<T> {
    /**
    * @see WSBaseResp
    */

    private Integer type;
    private T data;
}
