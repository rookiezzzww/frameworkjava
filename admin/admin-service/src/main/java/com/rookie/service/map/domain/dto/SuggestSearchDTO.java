package com.rookie.service.map.domain.dto;

import lombok.Data;

/**
 * 关键字搜索的查询条件DTO
 */
@Data
public class SuggestSearchDTO {
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 城市id,用来填补关键词查询的region，因为填北京和110000是等价的
     */
    private String id;
    /**
     * 分页编码
     */
    private Integer pageIndex;
    /**
     * 分页数量
     */
    private Integer pageSize;
}
