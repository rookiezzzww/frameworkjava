package com.rookie.file.domain.vo;

import lombok.Data;

@Data
public class SignVO {
    /**
     * 签名
     */
    private String signature;//规则对应的签名

    private String host; //上传到哪个 OSS 地址

    private String pathPrefix;//文件名前缀

    private String xOSSCredential; //凭据范围，包含 AK ID、日期、地域等

    private String xOSSDate;//签名所用的请求时间

    private String policy;//	编码后的上传规则
}
