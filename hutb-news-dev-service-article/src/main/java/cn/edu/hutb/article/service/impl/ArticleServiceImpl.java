package cn.edu.hutb.article.service.impl;

import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.api.page.PageUtils;
import cn.edu.hutb.article.mapper.ArticleMapper;
import cn.edu.hutb.article.mapper.ArticleMapperCustomer;
import cn.edu.hutb.article.service.ArticleService;
import cn.edu.hutb.enums.ArticleAppointType;
import cn.edu.hutb.enums.ArticleReviewLevel;
import cn.edu.hutb.enums.ArticleReviewStatus;
import cn.edu.hutb.enums.YesOrNo;
import cn.edu.hutb.pojo.Article;
import cn.edu.hutb.pojo.Category;
import cn.edu.hutb.pojo.bo.NewArticleBO;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.ResponseStatusEnum;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 田章
 * @description
 * @date 2023/2/26
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleMapperCustomer articleMapperCustomer;

    @Autowired
    private Sid sid;

    @Transactional
    @Override
    public void save(NewArticleBO bo, Category category) {
        String articleId = sid.nextShort();

        Article article = new Article();
        BeanUtils.copyProperties(bo, article);

        article.setId(articleId);
        article.setCategoryId(category.getId());
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setCommentCounts(0);
        article.setReadCounts(0);

        article.setIsDelete(YesOrNo.NO.type);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());

        // 判断定时发布还是立即发布
        article.setPublishTime(ArticleAppointType.TIMING.type.equals(article.getIsAppoint())
                ? bo.getPublishTime() : new Date());

        if (articleMapper.insert(article) != 1) {
            throw new CustomException(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }

        // TODO 使用阿里云文本反垃圾审核对文章文本进行自动审核（暂时写死）
        String aliTextReviewResult = ArticleReviewLevel.REVIEW.type;
        if (ArticleReviewLevel.PASS.type.equals(aliTextReviewResult)) {
            // 修改当前的文章，状态标记为审核通过
            this.updateArticleStatus(articleId, ArticleReviewStatus.SUCCESS.type);
        } else if (ArticleReviewLevel.REVIEW.type.equals(aliTextReviewResult)) {
            // 修改当前的文章，状态标记为需要人工审核
            this.updateArticleStatus(articleId, ArticleReviewStatus.WAITING_MANUAL.type);
        } else if (ArticleReviewLevel.BLOCK.type.equals(aliTextReviewResult)) {
            // 修改当前的文章，状态标记为审核未通过
            this.updateArticleStatus(articleId, ArticleReviewStatus.FAILED.type);
        }
    }

    @Transactional
    @Override
    public void updateAppointToPublish() {
        articleMapperCustomer.updateAppointToPublish();
    }

    @Override
    public PageResult listMyArticleByCondition(String userId,
                                               String keyword,
                                               Integer status,
                                               Date startDate,
                                               Date endDate,
                                               int page,
                                               int pageSize) {
        Example example = new Example(Article.class);
        // 排序规则
        example.orderBy("createTime").desc();

        // 条件表达式
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("publishUserId", userId)
                .andEqualTo("isDelete", YesOrNo.NO.type);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        if (ArticleReviewStatus.validStatus(status)) {
            criteria.andEqualTo("articleStatus", status);
        }
        // 前端约定好，12为审核中
        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
        }
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("publishTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("publishTime", endDate);
        }

        PageHelper.startPage(page, pageSize);
        return PageUtils.setterPage(articleMapper.selectByExample(example), page);
    }

    /**
     * 更改文章审核状态
     */
    @Transactional
    @Override
    public void updateArticleStatus(String articleId, Integer pendingStatus) {
        Example example = new Example(Article.class);
        example.createCriteria().andEqualTo("id", articleId);

        Article pendingArticle = new Article();
        pendingArticle.setArticleStatus(pendingStatus);

        if (articleMapper.updateByExampleSelective(pendingArticle, example) != 1) {
            throw new CustomException(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    @Override
    public PageResult listByStatus(Integer status, int page, int pageSize) {
        Example articleExample = new Example(Article.class);
        articleExample.orderBy("createTime").desc();

        Example.Criteria criteria = articleExample.createCriteria().andEqualTo("isDelete", YesOrNo.NO.type);
        if (ArticleReviewStatus.validStatus(status)) {
            criteria.andEqualTo("articleStatus", status);
        }

        // 审核中是机审和人审核的两个状态，所以需要单独判断
        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
        }

        PageHelper.startPage(page, pageSize);
        return PageUtils.setterPage(articleMapper.selectByExample(articleExample), page);
    }

    @Transactional
    @Override
    public void deleteArticle(String userId, String articleId) {
        Article pending = new Article();
        pending.setIsDelete(YesOrNo.YES.type);
        if (articleMapper.updateByExampleSelective(pending, makeExample(userId, articleId)) != 1) {
            throw new CustomException(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
    }

    @Transactional
    @Override
    public void withdrawArticle(String userId, String articleId) {
        Article pending = new Article();
        pending.setArticleStatus(ArticleReviewStatus.WITHDRAW.type);
        if (articleMapper.updateByExampleSelective(pending, makeExample(userId, articleId)) != 1) {
            throw new CustomException(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
    }

    private Example makeExample(String userId, String articleId) {
        Example articleExample = new Example(Article.class);
        articleExample.createCriteria()
                .andEqualTo("publishUserId", userId)
                .andEqualTo("id", articleId);
        return articleExample;
    }
}
