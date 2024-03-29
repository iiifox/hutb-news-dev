package cn.edu.hutb.article.service.impl;

import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.api.page.PageUtils;
import cn.edu.hutb.article.mapper.ArticleMapper;
import cn.edu.hutb.article.service.ArticlePortalService;
import cn.edu.hutb.enums.ArticleReviewStatus;
import cn.edu.hutb.enums.YesOrNo;
import cn.edu.hutb.pojo.Article;
import cn.edu.hutb.pojo.vo.ArticleDetailVO;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 田章
 * @description
 * @date 2023/2/26
 */
@Service
public class ArticlePortalServiceImpl implements ArticlePortalService {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public PageResult listIndexArticle(String keyword, Integer category, int page, int pageSize) {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = getDefaultArticleCriteria(articleExample);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        if (category != null) {
            criteria.andEqualTo("categoryId", category);
        }

        PageHelper.startPage(page, pageSize);
        return PageUtils.setterPage(articleMapper.selectByExample(articleExample), page);
    }

    @Override
    public List<Article> listHot() {
        Example articleExample = new Example(Article.class);
        getDefaultArticleCriteria(articleExample);

        PageHelper.startPage(1, 5);
        return articleMapper.selectByExample(articleExample);
    }

    @Override
    public PageResult queryArticleListOfWriter(String writerId, int page, int pageSize) {
        Example articleExample = new Example(Article.class);
        getDefaultArticleCriteria(articleExample).andEqualTo("publishUserId", writerId);

        PageHelper.startPage(page, pageSize);
        return PageUtils.setterPage(articleMapper.selectByExample(articleExample), page);
    }

    @Override
    public PageResult queryGoodArticleListOfWriter(String writerId) {
        return queryArticleListOfWriter(writerId, 1, 5);
    }

    @Override
    public ArticleDetailVO getDetail(String articleId) {
        Example articleExample = new Example(Article.class);
        getDefaultArticleCriteria(articleExample).andEqualTo("id", articleId);

        Article article = articleMapper.selectOneByExample(articleExample);
        ArticleDetailVO detailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(article, detailVO);
        detailVO.setCover(article.getArticleCover());
        return detailVO;
    }

    private Example.Criteria getDefaultArticleCriteria(Example articleExample) {
        articleExample.orderBy("publishTime").desc();
        // 查询首页文章的自带隐性查询条件：已发布 + 未删除 + 审核通过
        return articleExample.createCriteria()
                .andEqualTo("isAppoint", YesOrNo.NO.type)
                .andEqualTo("isDelete", YesOrNo.NO.type)
                .andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);
    }
}
