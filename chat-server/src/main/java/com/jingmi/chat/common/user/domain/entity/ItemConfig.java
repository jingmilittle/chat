package com.jingmi.chat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 功能物品配置表
 * </p>
 *
 * @author <a href="https://github.com/jingmilittle">jingmi</a>
 * @since 2024-07-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("item_config")
public class ItemConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId("id")
    private Long id;

    /**
     * 物品类型 1改名卡 2徽章
     */
    @TableField("type")
    private Integer type;

    /**
     * 物品图片
     */
    @TableField("img")
    private String img;

    /**
     * 物品功能描述
     */
    @TableField("describe")
    private String describe;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


}
