package cn.edu.hutb.api.controller.article;

import cn.edu.hutb.pojo.bo.NewArticleBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;

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

    /**
     * 根据条件查询用户的文章列表
     *
     * @param keyword   查询关键字
     * @param status    文章状态
     * @param startDate 按文章发布时间查 --> 开始日期
     * @param endDate   按文章发布时间查 --> 截至日期
     * @param page      展示的第几页
     * @param pageSize  每页数据条数
     * @return 分页返回
     */
    @PostMapping("/queryMyList")
    JSONResult queryMyArticlesByCondition(@RequestParam String userId,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) Integer status,
                                          @RequestParam(required = false) Date startDate,
                                          @RequestParam(required = false) Date endDate,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer pageSize);

    /**
     * 管理员查询用户的所有文章列表
     *
     * @param status   文章状态
     * @param page     展示的第几页
     * @param pageSize 每页数据条数
     * @return 分页返回
     */
    @PostMapping("/queryAllList")
    JSONResult queryArticlesByStatus(@RequestParam(required = false) Integer status,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer pageSize);

    /**
     * 管理员人工审核文章（通过或失败）
     *
     * @param articleId 文章id
     * @param passOrNot 0:审核失败 1:审核成功
     */
    @PostMapping("/doReview")
    JSONResult manualReview(@RequestParam String articleId, @RequestParam Integer passOrNot);

    /**
     * 用户删除文章
     *
     * @param userId    用户id
     * @param articleId 文章id
     */
    @PostMapping("/delete")
    JSONResult delete(@RequestParam String userId, @RequestParam String articleId);

    /**
     * 用户撤回文章
     *
     * @param userId    用户id
     * @param articleId 文章id
     */
    @PostMapping("/withdraw")
    JSONResult withdraw(@RequestParam String userId, @RequestParam String articleId);
}
