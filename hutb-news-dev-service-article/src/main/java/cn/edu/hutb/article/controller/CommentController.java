package cn.edu.hutb.article.controller;

import cn.edu.hutb.api.controller.BaseController;
import cn.edu.hutb.api.controller.article.CommentControllerApi;
import cn.edu.hutb.article.service.CommentService;
import cn.edu.hutb.constant.PageConsts;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.pojo.bo.CommentReplyBO;
import cn.edu.hutb.pojo.vo.AppUserVO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 田章
 * @description 评论相关业务的Controller
 * @date 2023/3/19
 */
@RestController
public class CommentController extends BaseController
        implements CommentControllerApi {

    @Autowired
    private CommentService commentService;

    @Override
    public JSONResult createComment(CommentReplyBO bo, BindingResult result) {
        // 判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            return JSONResult.errorMap(getErrors(result));
        }

        // 根据留言用户的id查询其昵称，用于存入到数据表进行字段的冗余处理，从而避免多表关联查询
        String userId = bo.getCommentUserId();
        // 发起restTemplate调用用户服务，获得用户的昵称
        AppUserVO appUserVO = getPublisherList(userId).get(0);
        String nickname = appUserVO.getNickname();
        String face = appUserVO.getFace();

        // 保存用户评论信息到数据库
        commentService.insertComment(bo.getArticleId(), bo.getFatherId(), bo.getContent(), userId, nickname, face);
        return JSONResult.ok();
    }

    @Override
    public JSONResult commentCounts(String articleId) {
        return JSONResult.ok(getCountsFromRedis(String.format(RedisConsts.ARTICLE_COMMENT_COUNTS_FORMATTER, articleId)));
    }

    @Override
    public JSONResult list(String articleId, Integer page, Integer pageSize) {
        return JSONResult.ok(commentService.listArticleComment(articleId,
                (page == null) ? PageConsts.DEFAULT_PAGE_NUM : page,
                (pageSize == null) ? PageConsts.DEFAULT_PAGE_SIZE : pageSize));
    }

    @Override
    public JSONResult mng(String writerId, Integer page, Integer pageSize) {
        return JSONResult.ok(commentService.listWriterComment(writerId,
                (page == null) ? PageConsts.DEFAULT_PAGE_NUM : page,
                (pageSize == null) ? PageConsts.DEFAULT_PAGE_SIZE : pageSize));
    }

    @Override
    public JSONResult delete(String writerId, String commentId) {
        commentService.deleteComment(writerId, commentId);
        return JSONResult.ok();
    }
}
