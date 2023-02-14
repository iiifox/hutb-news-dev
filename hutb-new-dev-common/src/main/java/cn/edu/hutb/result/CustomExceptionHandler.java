package cn.edu.hutb.result;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 统一异常拦截处理
 * 可以针对异常的类型进行捕获，然后返回json信息到前端
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public JSONResult returnCustomException(CustomException e) {
        e.printStackTrace();
        return JSONResult.exception(e.getResponseStatusEnum());
    }

    /**
     * 文件上传大小限制
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public JSONResult returnMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return JSONResult.exception(ResponseStatusEnum.FILE_MAX_SIZE_ERROR);
    }

}
