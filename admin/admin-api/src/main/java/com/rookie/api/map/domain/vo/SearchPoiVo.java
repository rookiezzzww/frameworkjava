package com.rookie.api.map.domain.vo;

import lombok.Data;

/**
 * 关键词查询结果
 */
@Data
public class SearchPoiVo {
    /**
     * 地点名称
     */
    private String title;

    /**
     * 地点地址
     */
    private String address;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;
}
