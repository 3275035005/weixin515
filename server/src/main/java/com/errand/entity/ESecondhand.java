package com.errand.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 二手闲置信息表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ESecondhand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 物品名称
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 价格
     */
    private String price;

    /**
     * 管理用户id
     */
    private String userId;

    /**
     * 出售状态  0:未出售  1:已出售
     */
    private String status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String created;


    /**
     * 商品描述
     */
    private String details;

}
