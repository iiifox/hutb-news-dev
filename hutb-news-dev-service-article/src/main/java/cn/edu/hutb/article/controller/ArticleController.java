package cn.edu.hutb.article.controller;

import cn.edu.hutb.api.controller.BaseController;
import cn.edu.hutb.api.controller.article.ArticleControllerApi;
import cn.edu.hutb.article.service.ArticleService;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.enums.ArticleCoverType;
import cn.edu.hutb.pojo.Category;
import cn.edu.hutb.pojo.bo.NewArticleBO;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import cn.edu.hutb.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author 田章
 * @description 文章业务Controller
 * @date 2023/2/25
 */
@RestController
public class ArticleController extends BaseController
        implements ArticleControllerApi {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ArticleService articleService;

    @Override
    public JSONResult createArticle(NewArticleBO bo, BindingResult result) {
        // 判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            return JSONResult.errorMap(getErrors(result));
        }

        // 判断文章封面类型，单图必填，纯文字则设置为空
        if (ArticleCoverType.ONE_IMAGE.type.equals(bo.getArticleType())) {
            if (StringUtils.isBlank(bo.getArticleCover())) {
                return JSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        } else {
            bo.setArticleCover("");
        }

        // 判断分类id是否存在
        String allCategoryJson = redisTemplate.opsForValue().get(RedisConsts.ALL_CATEGORY);
        if (StringUtils.isBlank(allCategoryJson)) {
            return JSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }

        for (Category c : Objects.requireNonNull(JsonUtils.jsonToList(allCategoryJson, Category.class))) {
            if (bo.getCategoryId().equals(c.getId())) {
                articleService.save(bo, c);
                return JSONResult.ok();
            }
        }
        return JSONResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
    }
}
