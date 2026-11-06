package com.rookie.file.config;


import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyuncs.exceptions.ClientException;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS auto config
 *
 */
@Configuration
// 当storage.type的配置为oss时，加载当前类，后续可能会改成cos或其它存储服务
@ConditionalOnProperty(value = "storage.type", havingValue = "oss")
// 当括号中条件成立时，微服务启动的时候就会加载当前类。
public class OSSAutoConfiguration {

    /**
     * oss客户端
     */
    public OSSClient ossClient;

    /**
     * 初始化客户端
     * @param prop oss配置
     * @return ossclient
     * @throws ClientException  客户端异常
     */
    @Bean
    public OSSClient ossClient(OSSProperties prop) throws ClientException {

        // 创建凭证提供者，因为不可能谁都能用我的oss账户
        DefaultCredentialProvider credentialsProvider = CredentialsProviderFactory.newDefaultCredentialProvider(
                prop.getAccessKeyId(), prop.getAccessKeySecret());

        // 配置客户端参数
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setProtocol(Protocol.HTTPS);
        conf.setSignatureVersion(SignVersion.V4);

        //初始化OSS客户端
        // 使用内网endpoint进行上传 prop.getIntEndpoint()
        if (prop.getInternal()){
            ossClient = (OSSClient) OSSClientBuilder.create()
                    .endpoint(prop.getIntEndpoint())
                    .region(prop.getRegion())
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(conf)
                    .build();
        } else {
            ossClient = (OSSClient) OSSClientBuilder.create()
                    .endpoint(prop.getEndpoint())
                    .region(prop.getRegion())
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(conf)
                    .build();
        }

        return ossClient;
    }

    /**
     * 关闭客户端
     */
    @PreDestroy // 当容器销毁时，执行此方法,即当微服务停止时，执行此方法
    public void closeOSSClient() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }
}
