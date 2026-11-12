package com.rookie.api.config.domain.dto;

import lombok.Data;

@Data
public class DictionaryDataDTO {
    /**
     * 字典数据ID
     */
    private Long id;
    /**
     * 字典类型键
     */
    private String typeKey;
    /**
     * 字典数据键
     */
    private String dataKey;
    /**
     * 字典数据值
     */
    private String value;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 排序值
     */
    private Integer sort;
    /**
     * 字典数据状态
     */
    private Integer status;
}
