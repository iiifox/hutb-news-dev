package cn.edu.hutb.files.service.impl;

import cn.edu.hutb.files.service.UploadService;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 田章
 * @description 上传到FastDFS服务器
 * @date 2023/2/14
 */
@Service
public class FdfsUploadService implements UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    @Value("${cn.edu.hutb.fdfs-host}")
    private String host;

    @Override
    public String upload(MultipartFile file, String fileExtName) throws IOException {
        return host + storageClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null)
                .getFullPath();
    }
}
