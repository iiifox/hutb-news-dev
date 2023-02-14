package cn.edu.hutb.files.service.impl;

import cn.edu.hutb.files.service.UploadService;
import cn.edu.hutb.util.COSUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author 田章
 * @description 上传到腾讯云COS服务器
 * @date 2023/2/14
 */
@Service
public class COSUploadService implements UploadService {
    @Override
    public String upload(MultipartFile file, String fileExtName) throws Exception {
        String key = new StringBuilder(DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDateTime.now()))
                .append('/')
                .append(UUID.randomUUID().toString().replaceAll("-", ""))
                .append('.')
                .append(fileExtName).toString();
        return COSUtils.upload(file, key);
    }
}
