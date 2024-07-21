package com.jingmi.chat.common.user.service;

public interface LoginService {
    String login(Long uid);

    Long getValidUid(String token);

    void renewAlTokenIfNecessary(String token);
}
