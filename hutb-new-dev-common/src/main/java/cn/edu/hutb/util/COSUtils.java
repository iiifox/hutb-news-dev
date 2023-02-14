package cn.edu.hutb.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author 田章
 * @description 腾讯云COS工具类
 * @date 2023/2/14
 */
public class COSUtils {

    private static final String secretId;
    private static final String secretKey;
    /**
     * 存储桶对应的的地域名
     */
    private static final String regionName;
    /**
     * 存储桶名。格式为 BucketName-APPID
     */
    private static final String bucketName;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("qcloud");
        secretId = bundle.getString("secretId");
        secretKey = bundle.getString("secretKey");
        regionName = bundle.getString("regionName");
        bucketName = bundle.getString("bucketName");
    }

    /**
     * 文件上传到 Qcloud COS
     *
     * @param file 待上传的文件
     * @param key  对象键：要上传的到存储桶中的唯一标识
     * @throws IOException 获取文件流失败
     */
    public static String upload(MultipartFile file, String key) throws IOException {
        // 创建 COSClient 实例，这个实例用来后续调用请求
        COSClient cosClient = new COSClient(new BasicCOSCredentials(secretId, secretKey),
                new ClientConfig(new Region(regionName)));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
        // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        objectMetadata.setContentLength(file.getSize());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key,
                file.getInputStream(), objectMetadata);
        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);

        cosClient.putObject(putObjectRequest);
        // 确认本进程不再使用 cosClient 实例之后，关闭之
        cosClient.shutdown();

        // 属性全为final，直接使用String不会降低性能
        return "https://" + bucketName + ".cos." + regionName + ".myqcloud.com/" + key;
    }
}