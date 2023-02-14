package cn.edu.hutb.files.controller;

import cn.edu.hutb.api.controller.files.FileUploadControllerApi;
import cn.edu.hutb.files.service.UploadService;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 田章
 * @description
 * @date 2023/2/14
 */
@RestController
@Slf4j
public class FileUploadController implements FileUploadControllerApi {

    @Autowired
    private UploadService fdfsUploadService;

    @Override
    public JSONResult uploadFace(String userId, MultipartFile file) throws Exception {
        String filename;
        if (file == null || StringUtils.isBlank(filename = file.getOriginalFilename())) {
            return JSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        // 获取文件名后缀名，并进行判断
        String fileExtName = filename.substring(filename.lastIndexOf('.') + 1);
        if (!"jpg".equalsIgnoreCase(fileExtName) && !"jpeg".equalsIgnoreCase(fileExtName)
                && !"png".equalsIgnoreCase(fileExtName)) {
            return JSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILED);
        }
        // 文件上传
        return JSONResult.ok(fdfsUploadService.upload(file, fileExtName));
    }
}
