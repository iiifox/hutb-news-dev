package cn.edu.hutb.article.controller;

import cn.edu.hutb.api.controller.article.ArticlePortalControllerApi;
import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.article.service.ArticlePortalService;
import cn.edu.hutb.constant.PageConsts;
import cn.edu.hutb.pojo.Article;
import cn.edu.hutb.pojo.vo.AppUserVO;
import cn.edu.hutb.pojo.vo.IndexArticleVO;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.util.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
public class ArticlePortalController implements ArticlePortalControllerApi {

    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public JSONResult list(String keyword, Integer category, Integer page, Integer pageSize) {
        PageResult pageResult = articlePortalService.listIndexArticle(keyword, category,
                (page == null) ? PageConsts.DEFAULT_START_PAGE : page,
                (pageSize == null) ? PageConsts.DEFAULT_PAGE_SIZE : pageSize);
        return JSONResult.ok(rebuildArticle(pageResult));
    }

    @Override
    public JSONResult hotList() {
        return JSONResult.ok(articlePortalService.listHot());
    }

    private PageResult rebuildArticle(PageResult result) {
        List<Article> articleList = (List<Article>) result.getRows();

        // 构建发布者id列表
        Set<String> idSet = new HashSet<>();
        List<String> idList = new ArrayList<>();
        articleList.forEach(article -> {
            // 构建发布者的set
            idSet.add(article.getPublishUserId());
        });

        // 发起远程调用（restTemplate），请求用户服务获得用户（idSet 发布者）的列表
        List<AppUserVO> publisherList = getPublisherList(idSet);

        // 拼接两个list，重组文章列表
        List<IndexArticleVO> indexArticleList = new ArrayList<>();
        for (Article article : articleList) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            BeanUtils.copyProperties(article, indexArticleVO);
            // 从 publisherList 中获得发布者的基本信息
            indexArticleVO.setPublisherVO(getUserIfPublisher(article.getPublishUserId(), publisherList));
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
