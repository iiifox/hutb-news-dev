package cn.edu.hutb.article.service.impl;

import cn.edu.hutb.article.mapper.ArticleMapper;
import cn.edu.hutb.article.mapper.ArticleMapperCustomer;
import cn.edu.hutb.article.service.ArticleService;
import cn.edu.hutb.enums.ArticleAppointType;
import cn.edu.hutb.enums.ArticleReviewStatus;
import cn.edu.hutb.enums.YesOrNo;
import cn.edu.hutb.pojo.Article;
import cn.edu.hutb.pojo.Category;
import cn.edu.hutb.pojo.bo.NewArticleBO;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.ResponseStatusEnum;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Article article = new Article();
        BeanUtils.copyProperties(bo, article);

        article.setId(sid.nextShort());
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
    }

    @Transactional
    @Override
    public void updateAppointToPublish() {
        articleMapperCustomer.updateAppointToPublish();
    }
}
