package com.rookie.api.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典数据编辑请求DTO
 */
@Data
public class DictionaryDataEditReqDTO {
    @NotBlank(message = "字典数据键名不能为空")
    private String dataKey;
    @NotBlank(message = "字典数据不能为空")
    private String value;
    private String remark;
    private Integer sort;
}