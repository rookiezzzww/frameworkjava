package com.rookie.api.config.domain.dto;

import com.rookie.commondomain.domain.dto.BasePageReqDTO;
import lombok.Data;

@Data
public class DictionaryTypeListReqDTO extends BasePageReqDTO {
    /**
     * 字典类型值
     */
    private String value;
    /**
     * 字典类型键
     */
    private String typeKey;
}
