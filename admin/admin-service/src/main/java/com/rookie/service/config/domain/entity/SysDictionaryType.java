package com.rookie.service.config.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 字典类型表
 */
@Data
@TableName("sys_dictionary_type")
public class SysDictionaryType {
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型编码
     */
    private String typeKey;

    /**
     * 字典类型名称
     */
    private String value;

    /**
     * 备注
     */
    private String remark;

    /**
     * 字典类型状态 1正常 0停用
     */
    private Integer status;
}
