package com.jingmi.chat.common.user.controller;


import com.jingmi.chat.common.common.domain.vo.resp.ApiResult;
import com.jingmi.chat.common.common.interceptor.TokenInterceptor;
import com.jingmi.chat.common.common.utils.RequestHolder;
import com.jingmi.chat.common.user.domain.entity.vo.resp.UserInfoResp;
import com.jingmi.chat.common.user.domain.vo.ModifyNameReq;
import com.jingmi.chat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/jingmilittle">jingmi</a>
 * @since 2024-07-13
 */
@RestController
@RequestMapping("/capi/user")
@Api(value = "用户接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo(HttpServletRequest request){
        log.info("attribute==>{}", RequestHolder.get());
        UserInfoResp userInfo = userService.getUserInfo(RequestHolder.get().getUid());
        return ApiResult.success(userInfo);
    }

    @PutMapping("/changeName")
    public  ApiResult<Void> updateUserInfo(@Valid @RequestBody ModifyNameReq modifyNameReq){
        userService.modifyName
                (modifyNameReq.getName(),RequestHolder.get().getUid());
        return ApiResult.success();
    }




}

