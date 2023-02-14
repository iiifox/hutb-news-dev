package cn.edu.hutb.files.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 田章
 * @description
 * @date 2023/2/14
 */
public interface UploadService {

    /**
     * 上传文件
     *
     * @return 文件上传后的地址
     */
    String upload(MultipartFile file, String fileExtName) throws Exception;
}
