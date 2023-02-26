package cn.edu.hutb.article.service;

import cn.edu.hutb.pojo.Category;
import cn.edu.hutb.pojo.bo.NewArticleBO;

/**
 * @author 田章
 * @description
 * @date 2023/2/26
 */
public interface ArticleService {

    /**
     * 发布文章
     */
    void save(NewArticleBO bo, Category category);

    /**
     * 更新定时发布为即时发布
     */
    void updateAppointToPublish();
}
