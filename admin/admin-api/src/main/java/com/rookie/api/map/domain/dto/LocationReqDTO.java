package com.rookie.api.map.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 根据经纬度查询的请求体
 */
@Data
public class LocationReqDTO {
    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    private Double lat;

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private Double lng;

    /**
     * 格式化信息
     * @return 格式化后的经纬度
     */
    public String formatInfo() {
        return lat + "," + lng;
    }
}
