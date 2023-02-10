package cn.edu.hutb.component.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 田章
 * @description 阿里云配置类
 * @date 2023/2/10
 */
@Component
@PropertySource("classpath:aliyun.properties")
@ConfigurationProperties("aliyun")
@Data
public class AliyunResource {
    private String accessKeyId;
    private String accessKeySecret;
}
