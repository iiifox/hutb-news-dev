package cn.edu.hutb.article.service;

import cn.edu.hutb.api.page.PageResult;

public interface CommentService {

    /**
     * 发表评论
     *
     * @param articleId       文章id
     * @param fatherCommentId 评论父if
     * @param content         评论的内容
     * @param userId          评论的用户id
     * @param nickname        评论的用户昵称
     */
    void insertComment(String articleId, String fatherCommentId, String content, String userId, String nickname, String face);

    /**
     * 查询文章评论列表
     *
     * @param articleId 文章id
     */
    PageResult listArticleComment(String articleId, int page, int pageSize);

    /**
     * 查询我的评论管理列表
     */
    PageResult listWriterComment(String writerId, int page, int pageSize);

    /**
     * 删除评论
     */
    void deleteComment(String writerId, String commentId);
}
