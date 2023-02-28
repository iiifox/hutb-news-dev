package cn.edu.hutb.api.controller.article;

import cn.edu.hutb.result.JSONResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 门户端文章业务接口
 */
@RequestMapping("/portal/article")
public interface ArticlePortalControllerApi {

    /**
     * 首页查询文章列表
     */
    @GetMapping("/list")
    JSONResult list(@RequestParam(required = false) String keyword,
                    @RequestParam(required = false) Integer category,
                    @RequestParam(required = false) Integer page,
                    @RequestParam(required = false) Integer pageSize);


    /**
     * 首页查询热闻列表
     */
    @GetMapping("/hotList")
    JSONResult hotList();
}
