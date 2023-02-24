package cn.edu.hutb.util;

import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;

public class FileUtils {

    /**
     * 文件流下载，在浏览器展示
     *
     * @param response
     * @param file     文件从盘符开始的完整路径
     */
    public static void downloadFileByStream(HttpServletResponse response, File file) {
        String filePath = file.getPath();
        System.out.println("filePath = " + filePath);
        // 对encode过的filePath处理
        if (filePath.contains("%")) {
            try {
                filePath = URLDecoder.decode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        ServletOutputStream out = null;
        try (FileInputStream in = new FileInputStream(file)) {
            String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
            String fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            // 设置文件ContentType类型
            if ("jpg,jepg,gif,png".contains(fileType)) {    // 判断图片类型
                response.setContentType("image/" + fileType);
            } else if ("pdf".contains(fileType)) {          // 判断pdf类型
                response.setContentType("application/pdf");
            } else {                                        // 设置multipart
                response.setContentType("multipart/form-data");
            }
            out = response.getOutputStream();
            // 读取文件流
            byte[] buffer = new byte[1024 * 10];
            for (int len = in.read(buffer); len != -1; len = in.read(buffer)) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 文件转换为base64
     *
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] fileData = null;
        // 读取文件字节数组
        try (InputStream in = Files.newInputStream(file.toPath())) {
            fileData = new byte[in.available()];
            in.read(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码并且返回
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(fileData);
    }

}
