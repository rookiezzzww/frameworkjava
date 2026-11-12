package com.rookie.service.config.service;

import com.rookie.api.config.domain.dto.ArgumentAddReqDTO;

/**
 * 参数管理服务接口
 */
public interface ISysArgumentService {
    /**
     * 添加参数
     */
    Long add(ArgumentAddReqDTO argumentAddReqDTO);
}
