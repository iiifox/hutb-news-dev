package cn.edu.hutb.api.controller.article;

import cn.edu.hutb.pojo.bo.CommentReplyBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 田章
 * @description 评论相关业务的接口
 * @date 2023/3/19
 */
@RequestMapping("/comment")
public interface CommentControllerApi {

    /**
     * 用户评论
     */
    @PostMapping("/createComment")
    JSONResult createComment(@RequestBody @Valid CommentReplyBO bo, BindingResult result);

    /**
     * 查询用户评论数
     *
     * @param articleId 文章id
     */
    @GetMapping("/counts")
    JSONResult commentCounts(@RequestParam String articleId);

    /**
     * 查询文章的所有评论列表
     *
     * @param articleId 文章id
     * @param page      展示的第几页
     * @param pageSize  每页数据条数
     * @return 分页返回
     */
    @GetMapping("/list")
    JSONResult list(@RequestParam String articleId,
                    @RequestParam Integer page,
                    @RequestParam Integer pageSize);

    /**
     * 查询我的评论管理列表
     *
     * @param writerId 作家id
     * @param page     展示的第几页
     * @param pageSize 每页数据条数
     * @return 分页返回
     */
    @PostMapping("/mng")
    JSONResult mng(@RequestParam String writerId,
                   @RequestParam Integer page,
                   @RequestParam Integer pageSize);

    /**
     * 作者删除评论
     *
     * @param writerId  作家id
     * @param commentId 评论id
     */
    @PostMapping("/delete")
    JSONResult delete(@RequestParam String writerId, @RequestParam String commentId);
}

