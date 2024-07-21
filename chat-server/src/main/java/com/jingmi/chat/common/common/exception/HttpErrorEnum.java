package com.jingmi.chat.common.common.exception;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;

import com.google.common.base.Charsets;
import com.jingmi.chat.common.common.domain.vo.resp.ApiResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 业务校验异常码
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-26
 */
@AllArgsConstructor
@Getter
public enum HttpErrorEnum {
    ACCESS_DENIED(401, "登录失效，请重新登录"),
    ;
    private Integer httpCode;
    private String msg;




    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(this.getHttpCode());
        response.setContentType(ContentType.JSON.toString(Charsets.UTF_8));
        ApiResult responseData = ApiResult.fail(this.httpCode,this.msg );
        response.getWriter().write(JSONUtil.toJsonStr(responseData));
    }
}
