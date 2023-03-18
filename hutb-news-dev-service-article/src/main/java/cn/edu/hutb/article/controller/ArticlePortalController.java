package cn.edu.hutb.article.controller;

import cn.edu.hutb.api.controller.BaseController;
import cn.edu.hutb.api.controller.article.ArticlePortalControllerApi;
import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.article.service.ArticlePortalService;
import cn.edu.hutb.constant.PageConsts;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.pojo.Article;
import cn.edu.hutb.pojo.vo.AppUserVO;
import cn.edu.hutb.pojo.vo.ArticleDetailVO;
import cn.edu.hutb.pojo.vo.IndexArticleVO;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.util.IpUtils;
import cn.edu.hutb.util.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 田章
 * @description 门户端文章业务Controller
 * @date 2023/2/26
 */
@RestController
public class ArticlePortalController extends BaseController
        implements ArticlePortalControllerApi {

    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public JSONResult list(String keyword, Integer category, Integer page, Integer pageSize) {
        PageResult pageResult = articlePortalService.listIndexArticle(keyword, category,
                (page == null) ? PageConsts.DEFAULT_PAGE_NUM : page,
                (pageSize == null) ? PageConsts.DEFAULT_PAGE_SIZE : pageSize);
        return JSONResult.ok(rebuildArticle(pageResult));
    }

    @Override
    public JSONResult hotList() {
        return JSONResult.ok(articlePortalService.listHot());
    }

    @Override
    public JSONResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
        return JSONResult.ok(rebuildArticle(articlePortalService.queryArticleListOfWriter(writerId,
                (page == null) ? PageConsts.DEFAULT_PAGE_NUM : page,
                (pageSize == null) ? PageConsts.DEFAULT_PAGE_SIZE : pageSize)));
    }

    @Override
    public JSONResult queryGoodArticleListOfWriter(String writerId) {
        return JSONResult.ok(articlePortalService.queryGoodArticleListOfWriter(writerId));
    }

    @Override
    public JSONResult detail(String articleId) {
        ArticleDetailVO detailVO = articlePortalService.getDetail(articleId);

        HashSet<String> idSet = new HashSet<>();
        idSet.add(detailVO.getPublishUserId());
        List<AppUserVO> publisherList = getPublisherList(idSet);
        if (!publisherList.isEmpty()) {
            detailVO.setPublishUserName(publisherList.get(0).getNickname());
        }
        detailVO.setReadCounts(getCountsFromRedis(String.format(RedisConsts.ARTICLE_READ_COUNTS_FORMATTER, articleId)));

        return JSONResult.ok(detailVO);
    }

    @Override
    public JSONResult readArticle(String articleId, HttpServletRequest request) {
        // 设置针对当前用户ip的永久存在的key，存入redis，表示该ip的用户已经阅读过了，无法累加阅读量
        String remoteIp = IpUtils.getRemoteIp(request);
        redisTemplate.opsForValue().set(String.format(RedisConsts.ARTICLE_ALREADY_READ_FORMATTER, articleId, remoteIp), "y");
        redisTemplate.opsForValue().increment(String.format(RedisConsts.ARTICLE_READ_COUNTS_FORMATTER, articleId), 1);
        return null;
    }

    private PageResult rebuildArticle(PageResult result) {
        List<Article> articleList = (List<Article>) result.getRows();

        // 构建发布者id列表
        final Set<String> idSet = new HashSet<>();
        final List<String> idList = new ArrayList<>();
        articleList.forEach(article -> {
            // 构建发布者的set
            idSet.add(article.getPublishUserId());
            // 构建文章id的list
            idList.add(String.format(RedisConsts.ARTICLE_READ_COUNTS_FORMATTER, article.getId()));
        });
        // 发起Redis的mget批量查询api，获取对应的值
        List<String> readCountsList = redisTemplate.opsForValue().multiGet(idList);

        // 发起远程调用（restTemplate），请求用户服务获得用户（idSet 发布者）的列表
        List<AppUserVO> publisherList = getPublisherList(idSet);

        // 拼接两个list，重组文章列表
        List<IndexArticleVO> indexArticleList = new ArrayList<>();
        for (int i = 0; i < articleList.size(); i++) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            Article article = articleList.get(i);
            BeanUtils.copyProperties(article, indexArticleVO);
            // 从 publisherList 中获得发布者的基本信息
            indexArticleVO.setPublisherVO(getUserIfPublisher(article.getPublishUserId(), publisherList));
            // 设置文章的阅读数
            String readCounts = readCountsList.get(i);
            indexArticleVO.setReadCounts((readCounts == null) ? 0 : Integer.parseInt(readCounts));
            indexArticleList.add(indexArticleVO);
        }

        result.setRows(indexArticleList);
        return result;
    }

    private AppUserVO getUserIfPublisher(String publisherId, List<AppUserVO> publisherList) {
        for (AppUserVO user : publisherList) {
            if (user.getId().equals(publisherId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 发起远程调用，获得用户的基本信息
     */
    private List<AppUserVO> getPublisherList(Set<String> idSet) {
        JSONResult resp = restTemplate.getForObject(
                "http://user.hutbnews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet),
                JSONResult.class);

        List<AppUserVO> publisherList = null;
        assert resp != null;
        if (resp.getStatus() == 200) {
            String userJson = JsonUtils.objectToJson(resp.getData());
            publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }
        return publisherList;
    }

}
