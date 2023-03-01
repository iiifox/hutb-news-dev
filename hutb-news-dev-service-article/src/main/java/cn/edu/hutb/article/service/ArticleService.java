package cn.edu.hutb.article.service;

import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.pojo.Category;
import cn.edu.hutb.pojo.bo.NewArticleBO;

import java.util.Date;

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

    /**
     * 用户中心：根据条件查询我的文章列表
     */
    PageResult listMyArticleByCondition(String userId,
                                        String keyword,
                                        Integer status,
                                        Date startDate,
                                        Date endDate,
                                        int page,
                                        int pageSize);

    /**
     * 更改文章审核状态
     */
    void updateArticleStatus(String articleId, Integer pendingStatus);

    /**
     * 管理员查询文章列表
     */
    PageResult listByStatus(Integer status, int page, int pageSize);


    /**
     * 删除文章
     */
    void deleteArticle(String userId, String articleId);

    /**
     * 撤回文章
     */
    void withdrawArticle(String userId, String articleId);
}
