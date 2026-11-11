package com.rookie.api.map.domain.vo;

import lombok.Data;

/**
 * 根据经纬度查询的返回体
 */
@Data
public class RegionCityVo {
    /**
     * 城市ID
     */
    private Long id;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 城市全称
     */
    private String fullName;
}
