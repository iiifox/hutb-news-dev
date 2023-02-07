package cn.edu.hutb.user.controller;

import cn.edu.hutb.JSONResult;
import cn.edu.hutb.api.controller.HelloControllerApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 田章
 * @description 测试Controller
 * @date 2023/2/7
 */
@RestController
@Slf4j
public class HelloController implements HelloControllerApi {
    @Override
    public JSONResult hello() {
        // return "hello world~";
        return JSONResult.ok();
    }
}
