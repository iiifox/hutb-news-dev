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
     * 首页根据条件查询文章列表
     *
     * @param keyword  查询关键字
     * @param category 文章类型
     * @param page     展示的第几页
     * @param pageSize 每页数据条数
     * @return 分页返回
     */
    @GetMapping("/list")
    JSONResult list(@RequestParam(required = false) String keyword,
                    @RequestParam(required = false) Integer category,
                    @RequestParam(required = false) Integer page,
                    @RequestParam(required = false) Integer pageSize);


    /**
     * 首页查询热闻列表
     *
     * @return 分页返回近期热文
     */
    @GetMapping("/hotList")
    JSONResult hotList();

    /**
     * 查询作家发布的所有文章列表
     *
     * @param writerId 作家id
     * @param page     展示的第几页
     * @param pageSize 每页数据条数
     * @return 分页返回
     */
    @GetMapping("/queryArticleListOfWriter")
    JSONResult queryArticleListOfWriter(@RequestParam String writerId,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer pageSize);

    /**
     * 作家页面查询近期佳文
     *
     * @param writerId 作家id
     */
    @GetMapping("/queryGoodArticleListOfWriter")
    JSONResult queryGoodArticleListOfWriter(@RequestParam String writerId);
}
