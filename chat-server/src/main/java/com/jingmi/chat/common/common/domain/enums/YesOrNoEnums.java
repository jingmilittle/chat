package com.jingmi.chat.common.common.domain.enums;

import lombok.AllArgsConstructor;

import java.util.Locale;

/**
 * @program: jingmiChat
 * @description:
 * @author: JingMi
 * @create: 2024-07-21 10:54
 **/
public enum YesOrNoEnums {
    NO(0, "no"),
    YES(1, "yes");

    private final Integer status;
    private final String desc;

    // 使用 private 构造函数来强化枚举的不可变性
    private YesOrNoEnums(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    // 提供根据状态码获取枚举常量的方法，增强可维护性与易用性
    public static YesOrNoEnums getByStatus(Integer status) {
        for (YesOrNoEnums yesOrNo : YesOrNoEnums.values()) {
            if (yesOrNo.status.equals(status)) {
                return yesOrNo;
            }
        }
        throw new IllegalArgumentException("No constant with status " + status + " found");
    }

    // 获取枚举常量的状态码
    public Integer getStatus() {
        return status;
    }

    // 获取枚举常量的描述
    public String getDesc() {
        return desc;
    }

    // 为国际化支持预留方法，这里简化处理，实际应用中可能需要结合资源文件或其他机制
    public String getDesc(Locale locale) {
        // 根据 locale 返回相应的描述，这里仅作为示例
        // 实际应用中，可能需要查询资源文件或使用其他机制来实现多语言支持
        return desc;
    }
}

