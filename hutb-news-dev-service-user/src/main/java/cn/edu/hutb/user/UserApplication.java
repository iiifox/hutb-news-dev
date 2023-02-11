package cn.edu.hutb.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author 田章
 * @description 用户服务启动类
 * @date 2023/2/7
 */
@SpringBootApplication
@MapperScan("cn.edu.hutb.user.mapper")
@ComponentScan({"cn.edu.hutb", "org.n3r.idworker"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
