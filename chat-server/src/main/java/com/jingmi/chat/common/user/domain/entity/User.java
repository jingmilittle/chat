package com.jingmi.chat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author <a href="https://github.com/jingmilittle">jingmi</a>
 * @since 2024-07-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("user")
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    @TableField("name")
    private String name;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    @TableField("avatar")
    private String avatar;

    /**
     * 性别 1为男性，2为女性
     */
    @ApiModelProperty(value = "性别 1为男性，2为女性")
    @TableField("sex")
    private Integer sex;

    /**
     * 微信openid用户标识
     */
    @ApiModelProperty(value = "微信openid用户标识")
    @TableField("open_id")
    private String openId;

    /**
     * 在线状态 1在线 2离线
     */
    @ApiModelProperty(value = "在线状态 1在线 2离线")
    @TableField("active_status")
    private Integer activeStatus;

    /**
     * 最后上下线时间
     */
    @ApiModelProperty(value = "最后上下线时间")
    @TableField("last_opt_time")
    private Date lastOptTime;

    /**
     * ip信息
     */
    @ApiModelProperty(value = "ip信息")
    @TableField("ip_info")
    private String ipInfo;

    /**
     * 佩戴的徽章id
     */
    @ApiModelProperty(value = "佩戴的徽章id")
    @TableField("item_id")
    private Long itemId;

    /**
     * 使用状态 0.正常 1拉黑
     */
    @ApiModelProperty(value = "使用状态 0.正常 1拉黑")
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;


}
