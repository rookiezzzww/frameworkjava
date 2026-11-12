package com.rookie.api.config.domain.dto;

import com.rookie.commondomain.domain.dto.BasePageReqDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DictionaryDataListReqDTO extends BasePageReqDTO {
    @NotBlank(message = "字典类型键名不能为空")
    private String typeKey;

    private String value;
}
