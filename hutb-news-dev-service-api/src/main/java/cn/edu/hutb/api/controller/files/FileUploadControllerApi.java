package cn.edu.hutb.api.controller.files;

import cn.edu.hutb.result.JSONResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 田章
 * @description 文件上传接口
 * @date 2023/2/14
 */
@RequestMapping("/fs")
public interface FileUploadControllerApi {

    /**
     * 上传用户头像
     */
    @PostMapping("/uploadFace")
    JSONResult uploadFace(@RequestParam String userId, MultipartFile file) throws Exception;
}
