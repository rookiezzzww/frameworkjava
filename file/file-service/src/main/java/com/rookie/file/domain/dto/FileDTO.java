package com.rookie.file.domain.dto;

import lombok.Data;

@Data
public class FileDTO {
    private String url;//oss文件访问地址
    private String key;//oss文件路径，/目录/文件名.后缀名
    private String name;//文件名
}
