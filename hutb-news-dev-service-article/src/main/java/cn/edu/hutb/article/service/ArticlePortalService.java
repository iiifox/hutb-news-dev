package cn.edu.hutb.article.service;

import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.pojo.Article;

import java.util.List;

/**
 * @author 田章
 * @description
 * @date 2023/2/26
 */
public interface ArticlePortalService {

    /**
     * 首页查询文章列表
     */
    PageResult listIndexArticle(String keyword, Integer category, int page, int pageSize);

    /**
     * 首页查询热闻列表
     */
    List<Article> listHot();

    /**
     * 查询作家发布的所有文章列表
     */
    PageResult queryArticleListOfWriter(String writerId, int page, int pageSize);

    /**
     * 作家页面查询近期佳文
     */
    PageResult queryGoodArticleListOfWriter(String writerId);
}
