package cn.edu.hutb.api.controller.article;

import cn.edu.hutb.pojo.bo.NewArticleBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author 田章
 * @description 文章业务接口
 * @date 2023/2/25
 */
@RequestMapping("/article")
public interface ArticleControllerApi {

    /**
     * 用户发布文章
     */
    @PostMapping("/createArticle")
    JSONResult createArticle(@RequestBody @Valid NewArticleBO bo, BindingResult result);

}
