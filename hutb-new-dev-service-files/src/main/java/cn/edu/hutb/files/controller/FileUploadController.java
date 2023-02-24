package cn.edu.hutb.files.controller;

import cn.edu.hutb.api.controller.files.FileUploadControllerApi;
import cn.edu.hutb.files.service.UploadService;
import cn.edu.hutb.pojo.bo.NewAdminBO;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import cn.edu.hutb.util.FileUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

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

    @Autowired
    private GridFSBucket gridFSBucket;

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

    @Override
    public JSONResult uploadAdminFace(NewAdminBO bo) throws IOException {
        // 将图片的base64字符串转换为byte数组
        byte[] bytes = new BASE64Decoder().decodeBuffer(bo.getImg64());
        // 上传到GridFS中
        ObjectId objectId = gridFSBucket.uploadFromStream(bo.getUsername() + ".png",
                new ByteArrayInputStream(bytes));
        return JSONResult.ok(objectId.toString());
    }

    @Override
    public JSONResult getAdminFace(String faceId, HttpServletResponse response) {
        // 参数判断
        if (StringUtils.isBlank(faceId) || "null".equalsIgnoreCase(faceId)) {
            return JSONResult.errorCustom(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        // 人脸以流的形式响应给前端
        FileUtils.downloadFileByStream(response, getFaceByFaceId(faceId));
        return JSONResult.ok();
    }

    @Override
    public JSONResult readFace64InGridFS(String faceId) {
        return JSONResult.ok(FileUtils.fileToBase64(getFaceByFaceId(faceId)));
    }

    /**
     * 从GridFS中根据faceId查找用户人脸数据
     */
    private File getFaceByFaceId(String faceId) {
        GridFSFile fsFile = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId))).first();
        if (fsFile == null) {
            throw new CustomException(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        // 获取文件流，将文件保存到服务器临时目录
        File parent = new File("/workspace/temp_face");
        parent.mkdirs();
        File adminFace = new File(parent, fsFile.getFilename());
        try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(adminFace.toPath()));) {
            gridFSBucket.downloadToStream(new ObjectId(faceId), os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return adminFace;
    }
}
