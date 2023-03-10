package cn.edu.hutb.api.controller.files;

import cn.edu.hutb.pojo.bo.NewAdminBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 田章
 * @description 文件上传接口
 * @date 2023/2/14
 */
@RequestMapping("/fs")
public interface FileUploadControllerApi {

    /**
     * 上传用户头像
     *
     * @param userId 用户id
     * @param file   用户头像
     * @return 用户头像在服务器中的地址
     */
    @PostMapping("/uploadFace")
    JSONResult uploadFace(@RequestParam String userId, MultipartFile file) throws Exception;

    /**
     * 上传管理员人脸到GridFS
     *
     * @return objectId
     */
    @PostMapping("/uploadToGridFS")
    JSONResult uploadAdminFace(@RequestBody NewAdminBO bo) throws IOException;

    /**
     * 从GridFS中获取管理员人脸数据
     */
    @GetMapping("/readInGridFS")
    JSONResult getAdminFace(@RequestParam String faceId, HttpServletResponse response);

    /**
     * 根据faceId从GridFS中读取人脸图片，并且返回其base64（该接口仅供后端调用）
     */
    @GetMapping("/readFace64InGridFS")
    JSONResult readFace64InGridFS(String faceId);

    /**
     * 上传多个文件
     *
     * @param userId 用户id
     * @param files  文件
     * @return 文件在服务器中的地址列表
     */
    @PostMapping("/uploadSomeFiles")
    JSONResult uploadFiles(@RequestParam String userId, MultipartFile[] files) throws Exception;
}
