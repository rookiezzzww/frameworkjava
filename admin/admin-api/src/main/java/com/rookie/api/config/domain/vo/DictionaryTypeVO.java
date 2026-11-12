package com.rookie.api.config.domain.vo;


import lombok.Data;

/**
 * 字典类型查询的返回体基类
 */
@Data
public class DictionaryTypeVO {
    /**
     * 自增主键
     */
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
