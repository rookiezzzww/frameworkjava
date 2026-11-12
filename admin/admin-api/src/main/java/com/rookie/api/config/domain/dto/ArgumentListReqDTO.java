package com.rookie.api.config.domain.dto;

import com.rookie.commondomain.domain.dto.BasePageReqDTO;
import lombok.Data;

/**
 * 查看参数方法请求体
 */
@Data
public class ArgumentListReqDTO extends BasePageReqDTO {
    private String configKey;
    private String name;
}
